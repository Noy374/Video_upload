package com.example.video_upload.helper;

import lombok.Data;

@Data
public class VideoUploadInfo {
    private String username;
    private String videoName;
    private volatile double progress;

    public VideoUploadInfo(String username, String videoName) {
        this.username = username;
        this.videoName = videoName;
        this.progress = 0;
    }


}