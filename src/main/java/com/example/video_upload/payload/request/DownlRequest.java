package com.example.video_upload.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DownlRequest {

    private String accessToken;
    private Long id;
    @JsonCreator
    public DownlRequest(@JsonProperty("accessToken") String token,@JsonProperty("id") Long id) {
        this.id=id;
        accessToken = token;
    }
}
