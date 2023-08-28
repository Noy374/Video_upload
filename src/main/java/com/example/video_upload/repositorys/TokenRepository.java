package com.example.video_upload.repositorys;

import com.example.video_upload.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TokenRepository  extends JpaRepository<Token,Long> {


    Token getTokenByAccessToken(String token);



}
