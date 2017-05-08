/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package io.confluent.examples.streams.avro;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class SongPlayCount extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"SongPlayCount\",\"namespace\":\"io.confluent.examples.streams.avro\",\"fields\":[{\"name\":\"song_id\",\"type\":\"long\"},{\"name\":\"plays\",\"type\":\"long\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public long song_id;
  @Deprecated public long plays;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public SongPlayCount() {}

  /**
   * All-args constructor.
   */
  public SongPlayCount(java.lang.Long song_id, java.lang.Long plays) {
    this.song_id = song_id;
    this.plays = plays;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return song_id;
    case 1: return plays;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: song_id = (java.lang.Long)value$; break;
    case 1: plays = (java.lang.Long)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'song_id' field.
   */
  public java.lang.Long getSongId() {
    return song_id;
  }

  /**
   * Sets the value of the 'song_id' field.
   * @param value the value to set.
   */
  public void setSongId(java.lang.Long value) {
    this.song_id = value;
  }

  /**
   * Gets the value of the 'plays' field.
   */
  public java.lang.Long getPlays() {
    return plays;
  }

  /**
   * Sets the value of the 'plays' field.
   * @param value the value to set.
   */
  public void setPlays(java.lang.Long value) {
    this.plays = value;
  }

  /** Creates a new SongPlayCount RecordBuilder */
  public static io.confluent.examples.streams.avro.SongPlayCount.Builder newBuilder() {
    return new io.confluent.examples.streams.avro.SongPlayCount.Builder();
  }
  
  /** Creates a new SongPlayCount RecordBuilder by copying an existing Builder */
  public static io.confluent.examples.streams.avro.SongPlayCount.Builder newBuilder(io.confluent.examples.streams.avro.SongPlayCount.Builder other) {
    return new io.confluent.examples.streams.avro.SongPlayCount.Builder(other);
  }
  
  /** Creates a new SongPlayCount RecordBuilder by copying an existing SongPlayCount instance */
  public static io.confluent.examples.streams.avro.SongPlayCount.Builder newBuilder(io.confluent.examples.streams.avro.SongPlayCount other) {
    return new io.confluent.examples.streams.avro.SongPlayCount.Builder(other);
  }
  
  /**
   * RecordBuilder for SongPlayCount instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<SongPlayCount>
    implements org.apache.avro.data.RecordBuilder<SongPlayCount> {

    private long song_id;
    private long plays;

    /** Creates a new Builder */
    private Builder() {
      super(io.confluent.examples.streams.avro.SongPlayCount.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(io.confluent.examples.streams.avro.SongPlayCount.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.song_id)) {
        this.song_id = data().deepCopy(fields()[0].schema(), other.song_id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.plays)) {
        this.plays = data().deepCopy(fields()[1].schema(), other.plays);
        fieldSetFlags()[1] = true;
      }
    }
    
    /** Creates a Builder by copying an existing SongPlayCount instance */
    private Builder(io.confluent.examples.streams.avro.SongPlayCount other) {
            super(io.confluent.examples.streams.avro.SongPlayCount.SCHEMA$);
      if (isValidValue(fields()[0], other.song_id)) {
        this.song_id = data().deepCopy(fields()[0].schema(), other.song_id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.plays)) {
        this.plays = data().deepCopy(fields()[1].schema(), other.plays);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'song_id' field */
    public java.lang.Long getSongId() {
      return song_id;
    }
    
    /** Sets the value of the 'song_id' field */
    public io.confluent.examples.streams.avro.SongPlayCount.Builder setSongId(long value) {
      validate(fields()[0], value);
      this.song_id = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'song_id' field has been set */
    public boolean hasSongId() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'song_id' field */
    public io.confluent.examples.streams.avro.SongPlayCount.Builder clearSongId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'plays' field */
    public java.lang.Long getPlays() {
      return plays;
    }
    
    /** Sets the value of the 'plays' field */
    public io.confluent.examples.streams.avro.SongPlayCount.Builder setPlays(long value) {
      validate(fields()[1], value);
      this.plays = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'plays' field has been set */
    public boolean hasPlays() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'plays' field */
    public io.confluent.examples.streams.avro.SongPlayCount.Builder clearPlays() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public SongPlayCount build() {
      try {
        SongPlayCount record = new SongPlayCount();
        record.song_id = fieldSetFlags()[0] ? this.song_id : (java.lang.Long) defaultValue(fields()[0]);
        record.plays = fieldSetFlags()[1] ? this.plays : (java.lang.Long) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}