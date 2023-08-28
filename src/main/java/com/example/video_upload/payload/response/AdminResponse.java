package com.example.video_upload.payload.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminResponse {
    private String username;
    private String videoName;
    private String loadingStatus;
}
