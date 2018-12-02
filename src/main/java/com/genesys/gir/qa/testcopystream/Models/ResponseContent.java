package com.genesys.gir.qa.testcopystream.Models;

public class ResponseContent {
    private final int statusCode;
    private final String Message;

    public ResponseContent(int statusCode, String Message){
        this.statusCode=statusCode;
        this.Message=Message;
    }
    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return Message;
    }
}
