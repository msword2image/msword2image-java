package com.mswordtoimage;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;

public class MsWordToImageConvert {

    private final String apiUser;
    private final String apiKey;
    private Output output;
    private Input input;

    /**
     * @param apiUser The API username given from mswordtoimage.com
     * @param apiKey The API password
     */
    public MsWordToImageConvert(String apiUser, String apiKey) {
        this.apiUser = apiUser;
        this.apiKey = apiKey;
        this.output = null;
        this.input = null;
    }

    public void fromFile(String filename) {
        this.input = new Input(InputType.File, filename);
    }

    public void fromURL(String url) {
        this.input = new Input(InputType.URL, url);
    }

    public boolean toFile(String filename) throws IOException {
        return this.toFile(filename, OutputImageFormat.PNG);
    }

    public boolean toFile(String filename, OutputImageFormat imageFormat) throws IOException {
        this.output = new Output(OutputType.File, imageFormat, filename);
        return this.convertToFile();
    }

    public String toBase46EncodedString() {
        return this.toBase46EncodedString(OutputImageFormat.PNG);
    }

    public String toBase46EncodedString(OutputImageFormat imageFormat) {
        this.output = new Output(OutputType.Base64EncodedString, imageFormat);
        return this.convertToBase46EncodedString();
    }

    private void checkInput() {
        if (this.input == null) {
            throw new IllegalArgumentException("MsWordToImageConvert: Input was not set!");
        }
    }

    private void checkOutput() {
        if (this.output == null) {
            throw new IllegalArgumentException("MsWordToImageConvert: Output was not set!");
        }
    }

    private Map<InputType, Set<OutputType>> getAllowedInputOutputTypes() {
        Set<OutputType> allowedOutputTypes = new HashSet<>();
        allowedOutputTypes.add(OutputType.File);
        allowedOutputTypes.add(OutputType.Base64EncodedString);

        Map<InputType, Set<OutputType>> returnValue = new HashMap<>();
        returnValue.put(InputType.URL, allowedOutputTypes);
        returnValue.put(InputType.File, allowedOutputTypes);

        return returnValue;
    }

    private void checkInputAndOutputTypes() {
        if (!this.getAllowedInputOutputTypes().containsKey(this.input.getType())) {
            throw new IllegalArgumentException("MsWordToImageConvert: Conversion from " + this.input.getType().toString() + " is not supported");
        }

        Set<OutputType> supportedOutputTypes = this.getAllowedInputOutputTypes().get(this.input.getType());
        if (!supportedOutputTypes.contains(this.output.getType())) {
            throw new IllegalArgumentException("MsWordToImageConvert: Conversion from " + this.input.getType().toString() + " to " + this.output.getType().toString() + " is not supported");
        }
    }

    private void checkInputAndOutput() {
        this.checkInput();
        this.checkOutput();
    }

    private void checkConversionSanity() {
        this.checkInputAndOutput();
        this.checkInputAndOutputTypes();
    }
    
    private File getOutputFile() {
        return new File(this.output.getValue());
    }

    private String constructMsWordToImageAddress(Map<String, String> additionalParameters) throws UnsupportedEncodingException {
        String returnValue = "http://msword2image.com/convert?"
                + "apiUser=" + URLEncoder.encode(this.apiUser, "UTF-8") + "&"
                + "apiKey=" + URLEncoder.encode(this.apiKey, "UTF-8") + "&"
                + "format=" + URLEncoder.encode(this.output.getImageFormat().name(), "UTF-8");

        for (String key : additionalParameters.keySet()) {
            String value = additionalParameters.get(key);
            returnValue += "&" + key + "=" + URLEncoder.encode(value, "UTF-8");
        }

        return returnValue;
    }

    private boolean convertToFile() throws IOException {
        this.checkConversionSanity();

        if (this.input.getType().equals(InputType.File)) {
            return this.convertFromFileToFile();
        } else if (this.input.getType().equals(InputType.URL)) {
            return this.convertFromURLToFile();
        }

        return false;
    }

    private boolean convertFromURLToFile() throws UnsupportedEncodingException, IOException {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("url", this.input.getValue());
        
        FileUtils.copyURLToFile(new URL(this.constructMsWordToImageAddress(parameters)), this.getOutputFile());
        return true;
    }

    private boolean convertFromFileToFile() {
        // TODO: implment actual conversion
        return false;
    }

    private String convertToBase46EncodedString() {
        this.checkConversionSanity();

        // TODO: implment actual conversion
        return null;
    }
}
