/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package io.confluent.examples.streams.avro;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class PageView extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PageView\",\"namespace\":\"io.confluent.examples.streams.avro\",\"fields\":[{\"name\":\"user\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"page\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"industry\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"flags\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.String user;
  @Deprecated public java.lang.String page;
  @Deprecated public java.lang.String industry;
  @Deprecated public java.lang.String flags;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public PageView() {}

  /**
   * All-args constructor.
   */
  public PageView(java.lang.String user, java.lang.String page, java.lang.String industry, java.lang.String flags) {
    this.user = user;
    this.page = page;
    this.industry = industry;
    this.flags = flags;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return user;
    case 1: return page;
    case 2: return industry;
    case 3: return flags;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: user = (java.lang.String)value$; break;
    case 1: page = (java.lang.String)value$; break;
    case 2: industry = (java.lang.String)value$; break;
    case 3: flags = (java.lang.String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'user' field.
   */
  public java.lang.String getUser() {
    return user;
  }

  /**
   * Sets the value of the 'user' field.
   * @param value the value to set.
   */
  public void setUser(java.lang.String value) {
    this.user = value;
  }

  /**
   * Gets the value of the 'page' field.
   */
  public java.lang.String getPage() {
    return page;
  }

  /**
   * Sets the value of the 'page' field.
   * @param value the value to set.
   */
  public void setPage(java.lang.String value) {
    this.page = value;
  }

  /**
   * Gets the value of the 'industry' field.
   */
  public java.lang.String getIndustry() {
    return industry;
  }

  /**
   * Sets the value of the 'industry' field.
   * @param value the value to set.
   */
  public void setIndustry(java.lang.String value) {
    this.industry = value;
  }

  /**
   * Gets the value of the 'flags' field.
   */
  public java.lang.String getFlags() {
    return flags;
  }

  /**
   * Sets the value of the 'flags' field.
   * @param value the value to set.
   */
  public void setFlags(java.lang.String value) {
    this.flags = value;
  }

  /** Creates a new PageView RecordBuilder */
  public static io.confluent.examples.streams.avro.PageView.Builder newBuilder() {
    return new io.confluent.examples.streams.avro.PageView.Builder();
  }
  
  /** Creates a new PageView RecordBuilder by copying an existing Builder */
  public static io.confluent.examples.streams.avro.PageView.Builder newBuilder(io.confluent.examples.streams.avro.PageView.Builder other) {
    return new io.confluent.examples.streams.avro.PageView.Builder(other);
  }
  
  /** Creates a new PageView RecordBuilder by copying an existing PageView instance */
  public static io.confluent.examples.streams.avro.PageView.Builder newBuilder(io.confluent.examples.streams.avro.PageView other) {
    return new io.confluent.examples.streams.avro.PageView.Builder(other);
  }
  
  /**
   * RecordBuilder for PageView instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<PageView>
    implements org.apache.avro.data.RecordBuilder<PageView> {

    private java.lang.String user;
    private java.lang.String page;
    private java.lang.String industry;
    private java.lang.String flags;

    /** Creates a new Builder */
    private Builder() {
      super(io.confluent.examples.streams.avro.PageView.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(io.confluent.examples.streams.avro.PageView.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.user)) {
        this.user = data().deepCopy(fields()[0].schema(), other.user);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.page)) {
        this.page = data().deepCopy(fields()[1].schema(), other.page);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.industry)) {
        this.industry = data().deepCopy(fields()[2].schema(), other.industry);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.flags)) {
        this.flags = data().deepCopy(fields()[3].schema(), other.flags);
        fieldSetFlags()[3] = true;
      }
    }
    
    /** Creates a Builder by copying an existing PageView instance */
    private Builder(io.confluent.examples.streams.avro.PageView other) {
            super(io.confluent.examples.streams.avro.PageView.SCHEMA$);
      if (isValidValue(fields()[0], other.user)) {
        this.user = data().deepCopy(fields()[0].schema(), other.user);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.page)) {
        this.page = data().deepCopy(fields()[1].schema(), other.page);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.industry)) {
        this.industry = data().deepCopy(fields()[2].schema(), other.industry);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.flags)) {
        this.flags = data().deepCopy(fields()[3].schema(), other.flags);
        fieldSetFlags()[3] = true;
      }
    }

    /** Gets the value of the 'user' field */
    public java.lang.String getUser() {
      return user;
    }
    
    /** Sets the value of the 'user' field */
    public io.confluent.examples.streams.avro.PageView.Builder setUser(java.lang.String value) {
      validate(fields()[0], value);
      this.user = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'user' field has been set */
    public boolean hasUser() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'user' field */
    public io.confluent.examples.streams.avro.PageView.Builder clearUser() {
      user = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'page' field */
    public java.lang.String getPage() {
      return page;
    }
    
    /** Sets the value of the 'page' field */
    public io.confluent.examples.streams.avro.PageView.Builder setPage(java.lang.String value) {
      validate(fields()[1], value);
      this.page = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'page' field has been set */
    public boolean hasPage() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'page' field */
    public io.confluent.examples.streams.avro.PageView.Builder clearPage() {
      page = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'industry' field */
    public java.lang.String getIndustry() {
      return industry;
    }
    
    /** Sets the value of the 'industry' field */
    public io.confluent.examples.streams.avro.PageView.Builder setIndustry(java.lang.String value) {
      validate(fields()[2], value);
      this.industry = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'industry' field has been set */
    public boolean hasIndustry() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'industry' field */
    public io.confluent.examples.streams.avro.PageView.Builder clearIndustry() {
      industry = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'flags' field */
    public java.lang.String getFlags() {
      return flags;
    }
    
    /** Sets the value of the 'flags' field */
    public io.confluent.examples.streams.avro.PageView.Builder setFlags(java.lang.String value) {
      validate(fields()[3], value);
      this.flags = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'flags' field has been set */
    public boolean hasFlags() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'flags' field */
    public io.confluent.examples.streams.avro.PageView.Builder clearFlags() {
      flags = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    public PageView build() {
      try {
        PageView record = new PageView();
        record.user = fieldSetFlags()[0] ? this.user : (java.lang.String) defaultValue(fields()[0]);
        record.page = fieldSetFlags()[1] ? this.page : (java.lang.String) defaultValue(fields()[1]);
        record.industry = fieldSetFlags()[2] ? this.industry : (java.lang.String) defaultValue(fields()[2]);
        record.flags = fieldSetFlags()[3] ? this.flags : (java.lang.String) defaultValue(fields()[3]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
