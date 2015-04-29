package com.mswordtoimage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * This class is for converting Microsoft Word documents to image
 */
public class MsWordToImageConvert {

    private final String apiUser;
    private final String apiKey;
    private Output output;
    private Input input;

    /**
     * @param apiUser The API username given from msword2image.com
     * @param apiKey The API password
     */
    public MsWordToImageConvert(String apiUser, String apiKey) {
        this.apiUser = apiUser;
        this.apiKey = apiKey;
        this.output = null;
        this.input = null;
    }

    /**
     * Convert from the word document file
     * @param filename The relative or absolute path name
     */
    public void fromFile(String filename) {
        this.input = new Input(InputType.File, filename);
    }

    /**
     * Convert from URL of the word document
     * @param url The URL of the word document
     */
    public void fromURL(String url) {
        this.input = new Input(InputType.URL, url);
    }

    /**
     * Converts to given file in JPEG format
     * @param filename The filename to output the converted image
     * @return true on success, false on error.
     * @throws IOException 
     */
    public boolean toFile(String filename) throws IOException {
        return this.toFile(filename, OutputImageFormat.JPEG);
    }

    /**
     * Converts to given file in given image format
     * @param filename The filename to output the converted image
     * @param imageFormat The output image file format. Example: JPEG, PNG, GIF.
     * @return true on success, false on error
     * @throws IOException 
     */
    public boolean toFile(String filename, OutputImageFormat imageFormat) throws IOException {
        this.output = new Output(OutputType.File, imageFormat, filename);
        return this.convertToFile();
    }

    /**
     * Converts to base64 encoded string representing the JPEG file
     * @return The base 64 encoded string as image
     * @throws IOException 
     */
    public String toBase46EncodedString() throws IOException {
        return this.toBase46EncodedString(OutputImageFormat.JPEG);
    }

    /**
     * Converts to base64 encoded string in given image file format
     * @param imageFormat The format of the output image. Example: JPEG, PNG, GIF
     * @return The base64 encoded image string
     * @throws IOException 
     */
    public String toBase46EncodedString(OutputImageFormat imageFormat) throws IOException {
        this.output = new Output(OutputType.Base64EncodedString, imageFormat);
        return this.convertToBase46EncodedString();
    }

    /**
     * Checks if input was set
     */
    private void checkInput() {
        if (this.input == null) {
            throw new IllegalArgumentException("MsWordToImageConvert: Input was not set!");
        }
    }

    /**
     * Checks if output was set
     */
    private void checkOutput() {
        if (this.output == null) {
            throw new IllegalArgumentException("MsWordToImageConvert: Output was not set!");
        }
    }

    /**
     * @return The allowed input / output combinations
     */
    private Map<InputType, Set<OutputType>> getAllowedInputOutputTypes() {
        Set<OutputType> allowedOutputTypes = new HashSet<>();
        allowedOutputTypes.add(OutputType.File);
        allowedOutputTypes.add(OutputType.Base64EncodedString);

        Map<InputType, Set<OutputType>> returnValue = new HashMap<>();
        returnValue.put(InputType.URL, allowedOutputTypes);
        returnValue.put(InputType.File, allowedOutputTypes);

        return returnValue;
    }

    /**
     * Checks if the given input output combination is valid
     */
    private void checkInputAndOutputTypes() {
        if (!this.getAllowedInputOutputTypes().containsKey(this.input.getType())) {
            throw new IllegalArgumentException("MsWordToImageConvert: Conversion from " + this.input.getType().toString() + " is not supported");
        }

        Set<OutputType> supportedOutputTypes = this.getAllowedInputOutputTypes().get(this.input.getType());
        if (!supportedOutputTypes.contains(this.output.getType())) {
            throw new IllegalArgumentException("MsWordToImageConvert: Conversion from " + this.input.getType().toString() + " to " + this.output.getType().toString() + " is not supported");
        }
    }

    /**
     * Checks if both input and output was set
     */
    private void checkInputAndOutput() {
        this.checkInput();
        this.checkOutput();
    }

    /**
     * Checks if input and output is set,
     * And also checks if given input/output conversion is valid combination
     */
    private void checkConversionSanity() {
        this.checkInputAndOutput();
        this.checkInputAndOutputTypes();
    }

    /**
     * @return Gets the output file path as File object
     */
    private File getOutputFile() {
        return new File(this.output.getValue());
    }

    /**
     * Constructs msword2image.com convert address with following query string parameters:
     * 1. API username - Given by msword2image.com
     * 2. API key - Given by msword2image.com
     * 3. format - The output image file format. Default is JPEG
     * @param additionalParameters The additional query string parameters
     * @return Returns the constructed URL
     * @throws UnsupportedEncodingException 
     */
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

    /**
     * Handles conversion from * to file
     * @return True on success, false on error
     * @throws IOException 
     */
    private boolean convertToFile() throws IOException {
        this.checkConversionSanity();

        if (this.input.getType().equals(InputType.File)) {
            return this.convertFromFileToFile();
        } else if (this.input.getType().equals(InputType.URL)) {
            return this.convertFromURLToFile();
        }

        return false;
    }

    /**
     * Handles conversion from URL to file
     * @return True on success, false on error
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    private boolean convertFromURLToFile() throws UnsupportedEncodingException, IOException {
        return this.convertFromURLToFile(this.getOutputFile());
    }

    /**
     * Handles conversion from URL to file for a given output destination
     * @param dest The destination where the output image will be saved
     * @return True on success, false on error
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    private boolean convertFromURLToFile(File dest) throws UnsupportedEncodingException, IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("url", this.input.getValue());

        FileUtils.copyURLToFile(new URL(this.constructMsWordToImageAddress(parameters)), dest);
        return true;
    }

    /**
     * Handles conversion from * to base64 encoded image string
     * @return The base64 encoded image string
     * @throws IOException 
     */
    private String convertToBase46EncodedString() throws IOException {
        this.checkConversionSanity();

        if (this.input.getType().equals(InputType.File)) {
            return this.convertFromFileToBase46EncodedString();
        } else if (this.input.getType().equals(InputType.URL)) {
            return this.convertFromURLToBase46EncodedString();
        } else {
            throw new IllegalArgumentException("MsWordToImageConvert: Conversion from " + this.input.getType().toString() + " is not supported");
        }
    }
    
    /**
     * @return Creates a temp file that is deleted when program exits
     * @throws IOException 
     */
    private File getTempFile() throws IOException {
        File tempFile = File.createTempFile("mswtiConvert_", ".raw");
        tempFile.deleteOnExit();
        return tempFile;
    }
    
    /**
     * Encodes the contents of given file to base64 string and returns it
     * @param source The File object to encode
     * @return The base64 encoded string contents
     * @throws IOException 
     */
    private String base64EncodeContentsOfFile(File source) throws IOException {
        byte[] outputByteArray = FileUtils.readFileToByteArray(source);
        return Base64.getEncoder().encodeToString(outputByteArray);
    }

    /**
     * Handles conversion from URL to base64 encoded image string
     * @return The base 64 encoded image string
     * @throws IOException 
     */
    private String convertFromURLToBase46EncodedString() throws IOException {
        File tempFile = this.getTempFile();
        this.convertFromURLToFile(tempFile);
        return this.base64EncodeContentsOfFile(tempFile);
    }

    /**
     * @return Gets the input file path as File object
     */
    private File getInputFile() {
        return new File(this.input.getValue());
    }
    
    /**
     * Handles conversion from file to file with given File object
     * @param dest The output file object
     * @return True on success, false on error
     * @throws IOException 
     */
    private boolean convertFromFileToFile(File dest) throws IOException {
        if (!this.getInputFile().exists()) {
            throw new IllegalArgumentException("MsWordToImageConvert: Input file was not found at '" + this.input.getValue() + "'");
        }

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(this.constructMsWordToImageAddress(new HashMap<>()));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file_contents", new FileBody(this.getInputFile()));
        post.setEntity(builder.build());
        
        HttpResponse response = client.execute(post);

        HttpEntity entity = response.getEntity();
        try (FileOutputStream fileOS = new FileOutputStream(dest)) {
            entity.writeTo(fileOS);
            fileOS.flush();
        }
        return true;
    }

    /**
     * Handles conversion from File to File
     * @return True on success, false on error
     * @throws IOException 
     */
    private boolean convertFromFileToFile() throws IOException {
        return this.convertFromFileToFile(this.getOutputFile());
    }

    /**
     * Handles conversion from file to bas64 encoded string
     * @return 
     */
    private String convertFromFileToBase46EncodedString() throws IOException {
        File tempFile = this.getTempFile();
        this.convertFromFileToFile(tempFile);
        return this.base64EncodeContentsOfFile(tempFile);
    }
}
