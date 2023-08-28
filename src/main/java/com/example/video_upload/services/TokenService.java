package com.example.video_upload.services;


import com.example.video_upload.entity.Token;
import com.example.video_upload.repositorys.TokenRepository;
import com.example.video_upload.repositorys.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@AllArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    public Token createToken(String token){
        Token newToken=new Token();
        newToken.setAccessToken(token);
        tokenRepository.save(newToken);
        return newToken;
    }


    @Transactional
    public  void deleteToken(String token,String username) {
        Token tokenEntity = tokenRepository.getTokenByAccessToken(token);
        if (tokenEntity != null) {
            userRepository.updateToken(username,null);
            tokenRepository.delete(tokenEntity);
        }
    }
    public Token getTokenByAccessToken(String token){
            return tokenRepository.getTokenByAccessToken(token);
    }

}