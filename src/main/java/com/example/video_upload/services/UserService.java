package com.example.video_upload.services;


import com.example.video_upload.entity.User;
import com.example.video_upload.entity.Video;
import com.example.video_upload.repositorys.TokenRepository;
import com.example.video_upload.repositorys.UserRepository;
import com.example.video_upload.repositorys.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private  final VideoRepository videoRepository;

    private final TokenRepository tokenRepository;
    public UserService(UserRepository userRepository, VideoRepository videoRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.tokenRepository = tokenRepository;
    }

    public boolean CheckAccessToVideo(Long id, User user) {
        List<Video> videosByUser = videoRepository.getVideoByUser(user);
        for (Video video:
             videosByUser) {
            if(video.getId()==id)return  true;
        }
        return false;
    }

    public boolean CreatUser(User user){
        if(userRepository.getUserByUsername(user.getUsername()).isEmpty()) {
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public User getUser(User user) {
        Optional<User> optionalUser = userRepository.getUserByUsername(user.getUsername());
        if( optionalUser.isEmpty())return null;
       return optionalUser.get();
    }

    public void updateToken(User user) {
        userRepository.updateToken(user.getUsername(), user.getToken());
    }

    public User getUserByToken(String token) {


        return userRepository.getUserByToken(tokenRepository.getTokenByAccessToken(token));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
