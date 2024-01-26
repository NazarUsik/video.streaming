package com.test.task.videostream.service.video;

import com.test.task.videostream.binarystorage.MinioStorageService;
import com.test.task.videostream.data.entity.VideoMetadata;
import com.test.task.videostream.data.service.VideoMetadataService;
import com.test.task.videostream.exception.StorageException;
import com.test.task.videostream.model.security.User;
import com.test.task.videostream.model.statistics.StatisticsData;
import com.test.task.videostream.service.statistics.StatisticsService;
import com.test.task.videostream.util.Range;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultVideoService implements VideoService {
    private final MinioStorageService storageService;
    private final StatisticsService statisticsService;
    private final VideoMetadataService metadataService;

    @Override
    @Transactional
    public UUID save(VideoMetadata metadata, MultipartFile video) {
        try {
            metadata.setSize(video.getSize());
            metadata.setHttpContentType(video.getContentType());
            UUID uuid = metadataService.save(metadata);

            storageService.save(video, uuid);
            return uuid;
        } catch (Exception ex) {
            log.error("Exception occurred when trying to save the file", ex);
            throw new StorageException(ex);
        }
    }

    @Override
    public ChunkWithMetadata fetchChunk(UUID uuid, Range range) {
        sendStatistics(uuid, range);
        VideoMetadata fileMetadata = metadataService.byId(uuid.toString()).orElseThrow();
        return new ChunkWithMetadata(fileMetadata, readChunk(uuid, range, fileMetadata.getSize()));
    }

    private byte[] readChunk(UUID uuid, Range range, long fileSize) {
        long startPosition = range.getRangeStart();
        long endPosition = range.getRangeEnd(fileSize);
        int chunkSize = (int) (endPosition - startPosition + 1);
        try (InputStream inputStream = storageService.getInputStream(uuid, startPosition, chunkSize)) {
            return inputStream.readAllBytes();
        } catch (Exception exception) {
            log.error("Exception occurred when trying to read file with ID = {}", uuid);
            throw new StorageException(exception);
        }
    }

    private void sendStatistics(UUID videoID, Range range) {
        if (range.getRangeStart() == 0) {
            statisticsService.sendStatistics(buildStatisticsData(videoID));
        }
    }

    private StatisticsData buildStatisticsData(UUID videoID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return StatisticsData.builder()
                .userId(user.id())
                .videoIds(Collections.singletonList(videoID.toString()))
                .action("play-video")
                .build();
    }

    public record ChunkWithMetadata(
            VideoMetadata metadata,
            byte[] chunk
    ) {
    }
}
