package com.mswordtoimage;

public class MsWordToImageConvert {

    private final String apiUser;
    private final String apiKey;
    private Output output;
    private Input input;
    
    /**
     * @param apiUser The API username given from mswordtoimage.com
     * @param apiKey  The API password
     */
    public MsWordToImageConvert(String apiUser, String apiKey) {
        this.apiUser = apiUser;
        this.apiKey  = apiKey;
        this.output  = null;
        this.input   = null;
    }
    
    public void fromFile(String filename) {
        this.input = new Input(InputType.File, filename);
    }
    
    public void fromURL(String url) {
        this.input = new Input(InputType.URL, url);
    }
    
    public boolean toFile(String filename) {
        return this.toFile(filename, OutputImageFormat.PNG);
    }
    
    public boolean toFile(String filename, OutputImageFormat imageFormat) {
        this.output = new Output(OutputType.File, imageFormat, filename);
    }
    
    public String toBase46EncodedString() {
        return this.toBase46EncodedString(OutputImageFormat.PNG);
    }
    
    public String toBase46EncodedString(OutputImageFormat imageFormat) {
        this.output = new Output(OutputType.Base64EncodedString, imageFormat);
    }
}
