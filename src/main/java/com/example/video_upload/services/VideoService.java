package com.example.video_upload.services;

import com.example.video_upload.entity.User;
import com.example.video_upload.entity.Video;
import com.example.video_upload.entity.VideoDownload;
import com.example.video_upload.repositorys.VideoDownloadRepository;
import com.example.video_upload.repositorys.VideoRepository;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoDownloadRepository videoDownloadRepository;

    public VideoService(VideoRepository videoRepository, VideoDownloadRepository videoDownloadRepository) {
        this.videoRepository = videoRepository;
        this.videoDownloadRepository = videoDownloadRepository;

    }


    @Value("${video.directory}")
    private String videoDirectory;



    public void saveVideo(MultipartFile file, User user, VideoDownload videoDownload) throws IOException, NoSuchAlgorithmException {
        byte[] bytes = file.getBytes();
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        byte[] md5HashBytes = md5Digest.digest(bytes);
        String hash= new String(Hex.encodeHex(md5HashBytes));
        long count = videoRepository.countByHash(hash);

        if (count==0) {

            saveVideoToFile(file, hash,videoDownload);
        }
            Video video = new Video();
            video.setName(file.getOriginalFilename());
            video.setHash(hash);
            video.setSize(file.getSize());

        video.setUser(user);

        videoRepository.save(video);


    }
    public void saveVideoToFile(MultipartFile file, String hash, VideoDownload videoDownload) throws IOException {
        String hashedFileName = hash + ".mp4";
        Path filePath = Paths.get(videoDirectory, hashedFileName);

        OutputStream out = Files.newOutputStream(filePath);
        InputStream in = file.getInputStream();

        byte[] buffer = new byte[1024*8];
        long fileSize = file.getSize();
        long uploadedSize = 0;

        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            uploadedSize += bytesRead;
            int progress = (int) (uploadedSize * 100.0 / fileSize);

           videoDownloadRepository.updateProgressById(videoDownload.getId(),progress);
        }

        in.close();
        out.close();
    }

    public List<Video> getVideosByUser(User userByToken) {
       return videoRepository.getVideoByUser(userByToken);
    }

    public Video getVideoById(Long id) {
        return videoRepository.getReferenceById(id);
    }
}
