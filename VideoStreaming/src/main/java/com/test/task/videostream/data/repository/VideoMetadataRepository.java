package com.test.task.videostream.data.repository;

import com.test.task.videostream.data.entity.VideoMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoMetadataRepository extends JpaRepository<VideoMetadata, String> {
    List<VideoMetadata> findAllByAuthorIdAndActiveIsTrue(String authorId);

    List<VideoMetadata> findAllByActiveIsTrue();

    List<VideoMetadata> findAllByActiveIsFalse();
}
