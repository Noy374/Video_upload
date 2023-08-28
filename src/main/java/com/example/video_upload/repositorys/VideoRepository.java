package com.example.video_upload.repositorys;

import com.example.video_upload.entity.User;
import com.example.video_upload.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository <Video,Long>{
    Video findByHash(String hash);
    List<Video> getVideoByUser(User user);
}
