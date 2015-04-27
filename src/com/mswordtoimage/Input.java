package com.mswordtoimage;

public class Input {
  private final InputType type;
  private final String value;
  
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
