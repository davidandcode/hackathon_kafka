/*
 * Copyright 2017 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.confluent.examples.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.StateStoreSupplier;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.test.TestUtils;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.confluent.examples.streams.kafka.EmbeddedSingleNodeKafkaCluster;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end integration test that demonstrates how to remove duplicate records from an input
 * stream.
 *
 * Here, a stateful {@link org.apache.kafka.streams.kstream.Transformer} (from the Processor API)
 * detects and discards duplicate input records based on an "event id" that is included in each
 * input record.  This transformer is then included in a topology defined via the DSL.
 *
 * In this simplified example, the values of input records represent the event ID by which
 * duplicates will be detected.  In practice, record values would typically be a more complex data
 * structure, with perhaps one of the fields being such an event ID.  De-duplication by an event ID
 * is but one example of how to perform de-duplication in general.  The code example below can be
 * adapted to other de-duplication approaches.
 *
 * IMPORTANT:  The Apache Kafka project is currently working on supporting exactly-once semantics.
 * Once available, most use cases will no longer need to worry about duplicates or duplicate
 * processing because, typically, such duplicates only happen in the face of failures. That said,
 * there will still be some scenarios where the upstream data producers may generate duplicate
 * events under normal, non-failure conditions; in such cases, an event de-duplication approach as
 * shown in this example is helpful. https://cwiki.apache.org/confluence/display/KAFKA/KIP-98+-+Exactly+Once+Delivery+and+Transactional+Messaging
 * https://cwiki.apache.org/confluence/display/KAFKA/KIP-129%3A+Streams+Exactly-Once+Semantics
 *
 * Note: This example uses lambda expressions and thus works with Java 8+ only.
 */
public class EventDeduplicationLambdaIntegrationTest {

  @ClassRule
  public static final EmbeddedSingleNodeKafkaCluster CLUSTER = new EmbeddedSingleNodeKafkaCluster();

  private static String inputTopic = "inputTopic";
  private static String outputTopic = "outputTopic";

  @BeforeClass
  public static void startKafkaCluster() throws Exception {
    CLUSTER.createTopic(inputTopic);
    CLUSTER.createTopic(outputTopic);
  }

  /**
   * Discards duplicate records from the input stream.
   *
   * Duplicate records are detected based on an event ID;  in this simplified example, the record
   * value is the event ID.  The store remembers known event IDs in an associated state store.
   * To prevent the store from growing indefinitely, the transformer purges/expires event IDs from
   * the store after a certain amount of time.
   *
   * Note: This code is for demonstration purposes and was not tested for production usage.
   */
  private static class DeduplicationTransformer<K, V, E> implements Transformer<K, V, KeyValue<K, V>> {

    private ProcessorContext context;

    /**
     * Key: event ID
     * Value: timestamp (event-time) of the corresponding event when the event ID was seen for the
     * first time
     */
    private KeyValueStore<E, Long> eventIdStore;

    /**
     * How long to remember an event ID.  Event IDs that have been stored for longer than this
     * duration will eventually be purged from the store.
     */
    private final long maintainDurationMs = TimeUnit.MINUTES.toMillis(5);

    private final KeyValueMapper<K, V, E> idExtractor;

    /**
     * @param idExtractor extracts a unique identifier from a record by which we de-duplicate input
     *                    records; if it returns null, the record will not be considered for
     *                    de-duping but forwarded as-is.
     */
    DeduplicationTransformer(KeyValueMapper<K, V, E> idExtractor) {
      this.idExtractor = idExtractor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(final ProcessorContext context) {
      this.context = context;
      eventIdStore = (KeyValueStore<E, Long>) context.getStateStore("eventId-store");
      this.context.schedule(TimeUnit.MINUTES.toMillis(1));
    }

    @Override
    public KeyValue<K, V> transform(final K recordKey, final V recordValue) {
      final E eventId = idExtractor.apply(recordKey, recordValue);
      if (eventId != null) {
        if (isDuplicate(eventId)) {
          // Discard the record.
          return null;
        } else {
          remember(eventId, context.timestamp());
          // Forward the record downstream as-is.
          return KeyValue.pair(recordKey, recordValue);
        }
      }
      // Forward the record downstream as-is.
      return KeyValue.pair(recordKey, recordValue);
    }

    private boolean isDuplicate(final E eventId) {
      return eventIdStore.get(eventId) != null;
    }

    private void remember(final E eventId, final long eventTimestamp) {
      eventIdStore.put(eventId, eventTimestamp);
    }

    @Override
    public KeyValue<K, V> punctuate(final long currentStreamTimeMs) {
      purgeExpiredEventIds(currentStreamTimeMs);
      return null;
    }

    private void purgeExpiredEventIds(final long currentStreamTimeMs) {
      try (final KeyValueIterator<E, Long> iterator = eventIdStore.all()) {
        while (iterator.hasNext()) {
          final KeyValue<E, Long> entry = iterator.next();
          final long eventTimestamp = entry.value;
          if (hasExpired(eventTimestamp, currentStreamTimeMs)) {
            eventIdStore.delete(entry.key);
          }
        }
      }
    }

    private boolean hasExpired(final long eventTimestamp, final long currentStreamTimeMs) {
      return (currentStreamTimeMs - eventTimestamp) > maintainDurationMs;
    }

    @Override
    public void close() {
      // Note: The store should NOT be closed manually here via `eventIdStore.close()`!
      // The Kafka Streams API will automatically close stores when necessary.
    }

  }

  @Test
  public void shouldRemoveDuplicatesFromTheInput() throws Exception {
    String firstId = UUID.randomUUID().toString(); // e.g. "4ff3cb44-abcb-46e3-8f9a-afb7cc74fbb8"
    String secondId = UUID.randomUUID().toString();
    String thirdId = UUID.randomUUID().toString();
    List<String> inputValues = Arrays.asList(firstId, secondId, firstId, firstId, secondId, thirdId, thirdId, firstId, secondId);
    List<String> expectedValues = Arrays.asList(firstId, secondId, thirdId);

    //
    // Step 1: Configure and start the processor topology.
    //
    KStreamBuilder builder = new KStreamBuilder();

    Properties streamsConfiguration = new Properties();
    streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "deduplication-lambda-integration-test");
    streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, CLUSTER.bootstrapServers());
    streamsConfiguration.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass().getName());
    streamsConfiguration.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    // The commit interval for flushing records to state stores and downstream must be lower than
    // this integration test's timeout (30 secs) to ensure we observe the expected processing results.
    streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
    streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    // Use a temporary directory for storing state, which will be automatically removed after the test.
    streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, TestUtils.tempDirectory().getAbsolutePath());

    StateStoreSupplier deduplicationStoreSupplier = Stores.create("eventId-store")
        .withKeys(Serdes.String()) // must match the return type of the Transformer's id extractor
        .withValues(Serdes.Long())
        .persistent()
        .build();

    builder.addStateStore(deduplicationStoreSupplier);

    KStream<byte[], String> input = builder.stream(inputTopic);
    KStream<byte[], String> deduplicated = input.transform(
        // In this example, we assume that the record value as-is represents a unique event ID by
        // which we can perform de-duplication.  If your records are different, adapt the extractor
        // function as needed.
        () -> new DeduplicationTransformer<>((key, value) -> value),
        "eventId-store");
    deduplicated.to(outputTopic);

    KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);
    streams.start();

    //
    // Step 2: Produce some input data to the input topic.
    //
    Properties producerConfig = new Properties();
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CLUSTER.bootstrapServers());
    producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
    producerConfig.put(ProducerConfig.RETRIES_CONFIG, 0);
    producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
    producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    IntegrationTestUtils.produceValuesSynchronously(inputTopic, inputValues, producerConfig);

    //
    // Step 3: Verify the application's output data.
    //
    Properties consumerConfig = new Properties();
    consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, CLUSTER.bootstrapServers());
    consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "deduplication-integration-test-standard-consumer");
    consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
    consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    List<String> actualValues = IntegrationTestUtils.waitUntilMinValuesRecordsReceived(consumerConfig,
        outputTopic, expectedValues.size());
    streams.close();
    assertThat(actualValues).containsExactlyElementsOf(expectedValues);
  }

}