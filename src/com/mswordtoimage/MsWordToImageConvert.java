package com.mswordtoimage;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    public boolean toFile(String filename) {
        return this.toFile(filename, OutputImageFormat.PNG);
    }

    public boolean toFile(String filename, OutputImageFormat imageFormat) {
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

    private void checkCanWriteOutputFile() {
        File outputFile = new File(this.output.getValue());
        if (!outputFile.canWrite()) {
            throw new IllegalArgumentException("MsWordToImageConvert: Can't write to output file at " + this.output.getValue());
        }
    }

    private boolean convertToFile() {
        this.checkConversionSanity();
        this.checkCanWriteOutputFile();
        
        // TODO: implment actual conversion
        return false;
    }

    private String convertToBase46EncodedString() {
        this.checkConversionSanity();
        
        // TODO: implment actual conversion
        return null;
    }
}
