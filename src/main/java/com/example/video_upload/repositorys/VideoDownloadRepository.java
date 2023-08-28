package com.example.video_upload.repositorys;

import com.example.video_upload.entity.VideoDownload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface VideoDownloadRepository extends JpaRepository<VideoDownload,Long> {

    @Modifying
    @Query("UPDATE VideoDownload u SET u.progress=:progress WHERE u.id=:id")
    @Transactional
    void updateProgressById(Long id, int progress);
}
