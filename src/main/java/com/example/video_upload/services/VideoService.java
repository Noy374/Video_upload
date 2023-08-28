package com.example.video_upload.services;

import com.example.video_upload.entity.User;
import com.example.video_upload.entity.Video;
import com.example.video_upload.repositorys.UserRepository;
import com.example.video_upload.repositorys.VideoRepository;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserService userService;

    public VideoService(VideoRepository videoRepository,UserService userService) {
        this.videoRepository = videoRepository;
        this.userService = userService;
    }


    @Value("${video.directory}")
    private String videoDirectory;



    public void saveVideo(MultipartFile file,User user) throws IOException, NoSuchAlgorithmException {
        byte[] bytes = file.getBytes();
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        byte[] md5HashBytes = md5Digest.digest(bytes);
        String hash= new String(Hex.encodeHex(md5HashBytes));
        Video video = videoRepository.findByHash(hash);
        if (video == null) {

            saveVideoToFile(file, hash);
        }
            video = new Video();
            video.setName(file.getOriginalFilename());
            video.setHash(hash);
            video.setSize(file.getSize());

        video.setUser(user);

        videoRepository.save(video);


    }
    public void saveVideoToFile(MultipartFile file, String hash) throws IOException {
        String hashedFileName = hash + ".mp4";

        Path filePath = Paths.get(videoDirectory, hashedFileName);
        Files.write(filePath, file.getBytes());
    }

    public List<Video> getVideosByUser(User userByToken) {
       return videoRepository.getVideoByUser(userByToken);
    }

    public Video getVideoById(Long id) {
        return videoRepository.getReferenceById(id);
    }
}
