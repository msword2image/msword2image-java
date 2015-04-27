package com.mswordtoimage;

public class MsWordToImageConvert {

    private final String apiUser;
    private final String apiKey;
    
    /**
     * @param apiUser The API username given from mswordtoimage.com
     * @param apiKey  The API password
     */
    public MsWordToImageConvert(String apiUser, String apiKey) {
        this.apiUser = apiUser;
        this.apiKey  = apiKey;
    }
}
