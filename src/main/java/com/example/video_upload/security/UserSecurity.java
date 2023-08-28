package com.example.video_upload.security;


import com.example.video_upload.entity.User;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UserSecurity {

    public String GenerateAccessToken(User user){
        String value = user.getUsername();
        char[]arr=value.toCharArray();
        StringBuilder str =new StringBuilder();
        Random random = new Random();
        for (int i=0;i<value.length()/2;i++){
            str.append(Math.abs(arr[i]*random.nextInt()));
        }
        return str.toString();
    }

}