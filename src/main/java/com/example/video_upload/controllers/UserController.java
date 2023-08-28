package com.example.video_upload.controllers;


import com.example.video_upload.entity.User;
import com.example.video_upload.entity.Video;
import com.example.video_upload.entity.VideoDownload;
import com.example.video_upload.payload.request.DownlRequest;
import com.example.video_upload.payload.request.UserRequest;
import com.example.video_upload.payload.response.MessageResponse;
import com.example.video_upload.services.TokenService;
import com.example.video_upload.services.UserService;
import com.example.video_upload.services.VideoDownloadService;
import com.example.video_upload.services.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController

public class UserController {

    private final UserService userService;
    private final VideoService videoService;
    private final TokenService tokenService;
    private final VideoDownloadService videoDownloadService;
    private final ConcurrentHashMap<Long, AtomicInteger> userConcurrentUploads = new ConcurrentHashMap<>();
    private  final Map<Long, Integer> UPLOAD_PROGRESS = new ConcurrentHashMap<>();
    @Value("${video.directory}")
    private String videoDirectory;

    public UserController(UserService userService, VideoService videoService, TokenService tokenService, VideoDownloadService videoDownloadService) {
        this.userService = userService;
        this.videoService = videoService;
        this.tokenService = tokenService;
        this.videoDownloadService = videoDownloadService;
    }

    private void cleanupUserUploads() {
        userConcurrentUploads.entrySet().removeIf(entry -> entry.getValue().get() == 0);
    }
    @PostMapping("/addvideo")
    public ResponseEntity<Object> addVideo(
                                           @RequestParam("videoFile") MultipartFile videoFile,
                                           @RequestParam("accessToken") String token) {
        if (tokenService.getTokenByAccessToken(token)==null) return  ResponseEntity.badRequest().body(new MessageResponse("Log in again"));
        User user = userService.getUserByToken(token);

        AtomicInteger userUploads = userConcurrentUploads.computeIfAbsent(user.getId(), id -> new AtomicInteger(0));

        userUploads.incrementAndGet();

        try {

            if (!Objects.requireNonNull(videoFile.getContentType()).startsWith("video/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid file format. Only video files are allowed"));
            }

            if (userUploads.get() > 2)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("You can only upload 2 videos at a time"));


            VideoDownload videoDownload=new VideoDownload();
            videoDownload.setUsername(user.getUsername());
            videoDownload.setProgress(0);
            videoDownload.setVideoName(videoFile.getName());
            videoDownloadService.save(videoDownload);
            videoService.saveVideo(videoFile, user,videoDownload);
            videoDownloadService.delete(videoDownload);
            } catch (NoSuchAlgorithmException | IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Failed to upload video"));
            } finally {
                userUploads.decrementAndGet();
                cleanupUserUploads();
            }

        return ResponseEntity.ok(new MessageResponse("Video uploaded successfully"));
    }

    @GetMapping("/myvideos")
    public ResponseEntity<Object> getVideos(@RequestBody UserRequest request) {
        List<Video> videos = videoService.getVideosByUser(userService.getUserByToken(request.getAccessToken()));
        return ResponseEntity.ok().body(videos);
    }

    @PostMapping("/downlvideo")
    public ResponseEntity<Object> downlVideo(@RequestBody DownlRequest request) {
        try {

            Video video = videoService.getVideoById(request.getId());

            User user = userService.getUserByToken(request.getAccessToken());

            if (user==null)  return ResponseEntity.badRequest().body(new MessageResponse("Log in again"));

            if(!userService.CheckAccessToVideo(request.getId(),user))
               return ResponseEntity.badRequest().body(new MessageResponse( "Access to video is denied"));

            String hashedFileName = video.getHash() + ".mp4";

            Path filePath = Paths.get(videoDirectory, hashedFileName);
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(filePath));
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + video.getName() + "\"")
                    .body(resource);
        } catch (IOException e) {
           return ResponseEntity.badRequest().body(new MessageResponse( "An error occurred while downloading the video"));
        }

    }


    @GetMapping("/admin")
    public ResponseEntity<Object> admin(@RequestParam("accessToken") String accessToken) {
        String adminToken = "12345678"; // преполагается ,что у админа свой токен аутентификации
        if (adminToken.equals(accessToken)) {

            List<VideoDownload> info=videoDownloadService.getAll();
            return ResponseEntity.ok().body(info);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Access denied"));
        }
    }
}