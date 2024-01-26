package com.test.task.videostream.service.video;


import com.test.task.videostream.data.entity.VideoMetadata;
import com.test.task.videostream.util.Range;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface VideoService {

    UUID save(VideoMetadata videoMetadata, MultipartFile video);

    DefaultVideoService.ChunkWithMetadata fetchChunk(UUID uuid, Range range);
}
