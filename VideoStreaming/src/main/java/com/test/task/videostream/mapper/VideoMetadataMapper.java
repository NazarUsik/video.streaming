package com.test.task.videostream.mapper;

import com.test.task.videostream.config.MapStructConfig;
import com.test.task.videostream.data.entity.VideoMetadata;
import com.test.task.videostream.model.api.VideoInfo;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface VideoMetadataMapper {

    VideoMetadata map(VideoInfo info, String authorId);
}
