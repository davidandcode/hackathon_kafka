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

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Properties;
import java.util.concurrent.TimeUnit;


/**
 * End-to-end integration test that demonstrates how to perform a join between two KStreams.
 *
 * Note: This example uses lambda expressions and thus works with Java 8+ only.
 */
public class StreamToStreamJoinIntegration {

  public static void main(final String[] args) throws Exception {

    final String bootstrapServers = args.length > 0 ? args[0] : "localhost:9092";
    final Properties streamsConfiguration = new Properties();
    streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "tradekarma-2");
    streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    streamsConfiguration.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    streamsConfiguration.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());


    final Serde<String> stringSerde = Serdes.String();

    KStreamBuilder builder = new KStreamBuilder();
    KStream<String, String> alerts = builder.stream(stringSerde, stringSerde, "topic_price_valid");
    KStream<String, String> incidents = builder.stream(stringSerde, stringSerde, "topic_volume");

    alerts.foreach((key, value) -> System.out.println("2 key: " + key + " value: " + value));

    alerts.foreach((key, value) -> {
              try{
                postdataprice(Double.parseDouble(value));

              } catch (Exception e){}
            }
    );


    incidents.foreach((key, value) -> System.out.println("3 key: " + key + " value: " + value));

    incidents.foreach((key, value) -> {
              try{
                postdatavol(Double.parseDouble(value));

              } catch (Exception e){}
            }
    );

    KStream<String, String> impressionsAndClicks = alerts.join(incidents,
            (impressionValue, clickValue) -> Double.toString(Double.parseDouble(impressionValue) * Double.parseDouble(clickValue)),
            JoinWindows.of(TimeUnit.SECONDS.toMillis(20000)),
            stringSerde, stringSerde, stringSerde);


    impressionsAndClicks.foreach((key, value) -> System.out.println("final key: " + key + " value: " + value));


    impressionsAndClicks.foreach((key, value) -> {
              try{
                postdatanav(Double.parseDouble(value));

              } catch (Exception e){}
            }
    );

    // Write the results to the output topic.
    impressionsAndClicks.to(stringSerde, stringSerde, "topic_nav");

    KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);
    streams.cleanUp();
    streams.start();

    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

  public static void postdataprice( double value) throws Exception{
    Runtime.getRuntime().exec(new String[]{"bash","-c", String.format("curl -i -XPOST 'http://10.101.60.53:8443/write?db=etl_scala' --data-binary \"commodity_price,old=yes value=%f\"",value)});
  }

  public static void postdatavol( double value) throws Exception{
    Runtime.getRuntime().exec(new String[]{"bash","-c", String.format("curl -i -XPOST 'http://10.101.60.53:8443/write?db=etl_scala' --data-binary \"commodity_volume,old=yes value=%f\"",value)});
  }


  public static void postdatanav( double value) throws Exception{
    Runtime.getRuntime().exec(new String[]{"bash","-c", String.format("curl -i -XPOST 'http://10.101.60.53:8443/write?db=etl_scala' --data-binary \"commodity_nav,old=yes value=%f\"",value)});
  }

}
