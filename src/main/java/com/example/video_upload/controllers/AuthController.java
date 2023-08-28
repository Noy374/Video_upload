package com.example.video_upload.controllers;


import com.example.video_upload.entity.Token;
import com.example.video_upload.entity.User;
import com.example.video_upload.payload.request.LogOutRequest;
import com.example.video_upload.payload.response.LogInResponse;
import com.example.video_upload.payload.response.MessageResponse;
import com.example.video_upload.security.UserSecurity;
import com.example.video_upload.services.TokenService;
import com.example.video_upload.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserSecurity userSecurity;
    private final TokenService tokenService;

    @PostMapping("/reg")
    ResponseEntity<Object> Registration(@RequestBody User user){
       return userService.CreatUser(user) ?
               ResponseEntity.ok().body(new MessageResponse("Authorization was successful"))
               :ResponseEntity.badRequest().body(new MessageResponse("User with this username already exists"));
    }

    @PostMapping("/login")
    ResponseEntity<Object> LogIn(@RequestBody User user) {
        if( userService.getUser(user)!=null) {
            String accessToken=userSecurity.GenerateAccessToken(user);

            Token token =tokenService.createToken(accessToken);
            user.setToken(token);
            userService.updateToken(user);

                  return  ResponseEntity.ok().body(new LogInResponse(accessToken));
        }
            return ResponseEntity.badRequest().body(new MessageResponse("Username entered incorrectly"));
    }

    @PostMapping("/logout")
    ResponseEntity<Object> LogOut(@RequestBody LogOutRequest token) {
        tokenService.deleteToken(token.getAccessToken(),token.getUsername());
        return ResponseEntity.ok().body(new MessageResponse("Successful log out"));
    }




}
