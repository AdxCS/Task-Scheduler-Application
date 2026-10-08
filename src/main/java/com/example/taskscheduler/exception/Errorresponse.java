package com.example.taskscheduler.exception;
import java.time.Instant;
import java.util.Map;
public record Errorresponse(
    Instant timestamp,
    int status,
    String error,
    String message,
    Map<String, String> fieldErrors

){
    public static Errorresponse of(int status, String error, String message){
        return new Errorresponse(Instant.now(), status, error, message, null);
    }
    public static Errorresponse validation(int status,String error,Map<String, String> fieldErrors){
        return new Errorresponse(Instant.now(), status, error, "Validation Failed", fieldErrors);
    }
}
