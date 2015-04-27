package com.mswordtoimage;

public class Output {
  private final OutputType type;
  private final OutputImageFormat imageFormat;
  private final String value;
  
  public Output(OutputType type, OutputImageFormat imageFormat) {
      this(type, imageFormat, null);
  }
  
  public Output(OutputType type, OutputImageFormat imageFormat, String value) {
    this.type = type;
    this.imageFormat = imageFormat;
    this.value = value;
  }
  
  public OutputType getType() {
    return this.type;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public OutputImageFormat getImageFormat() {
    return this.imageFormat;
  }
}
