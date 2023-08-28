package com.example.video_upload.repositorys;

import com.example.video_upload.entity.Token;
import com.example.video_upload.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> getUserByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.token = :token WHERE u.username= :username")
    @Transactional
    void updateToken(@Param("username") String username, @Param("token") Token token);


    User getUserByToken(Token token);
}
