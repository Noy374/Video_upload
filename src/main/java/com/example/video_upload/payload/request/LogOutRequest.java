package com.example.video_upload.payload.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;




@Data
public class LogOutRequest {

    private String accessToken;
    private String username;
    @JsonCreator
    public LogOutRequest(@JsonProperty("accessToken") String token,@JsonProperty("username")String username) {
        accessToken = token;
        this.username=username;
    }
}
