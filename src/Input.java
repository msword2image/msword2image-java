package com.mswordtoimage;

public class Input {
  private InputType type;
  private String value;
  
  public Input(InputType type, String value) {
    this.type = type;
    this.value = value;
  }
  
  public InputType getType() {
    return this.type;
  }
  
  public String getValue() {
    return this.value;
  }
}
