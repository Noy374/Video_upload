package com.example.video_upload.services;

import com.example.video_upload.entity.VideoDownload;
import com.example.video_upload.repositorys.VideoDownloadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoDownloadService {
    private final VideoDownloadRepository videoDownloadRepository;

    public VideoDownloadService(VideoDownloadRepository videoDownloadRepository) {
        this.videoDownloadRepository = videoDownloadRepository;
    }

    public void save(VideoDownload videoDownload) {
        videoDownloadRepository.save(videoDownload);
    }

    public List<VideoDownload> getAll() {
        return videoDownloadRepository.findAll();
    }

    public void delete(VideoDownload videoDownload) {
        videoDownloadRepository.delete(videoDownload);
    }
}
