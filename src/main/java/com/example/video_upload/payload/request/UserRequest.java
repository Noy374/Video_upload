package com.example.video_upload.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRequest {
    private String accessToken;
    @JsonCreator
    public UserRequest(@JsonProperty("accessToken") String token) {
        accessToken = token;
    }
}
