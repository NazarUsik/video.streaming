package com.test.task.videostream.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.task.videostream.controller.constants.HttpConstants;
import com.test.task.videostream.mapper.VideoMetadataMapper;
import com.test.task.videostream.model.api.VideoInfo;
import com.test.task.videostream.model.security.User;
import com.test.task.videostream.service.video.DefaultVideoService;
import com.test.task.videostream.service.video.VideoService;
import com.test.task.videostream.util.Range;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.HttpHeaders.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideoStreamingController {
    private final VideoService videoService;
    private final VideoMetadataMapper videoMetadataMapper;
    private final ObjectMapper mapper;

    @Value("${app.streaming.default-chunk-size}")
    public Integer defaultChunkSize;

    @SneakyThrows
    @PostMapping("/save")
    public ResponseEntity<UUID> save(
            Authentication authentication,
            @RequestParam("file") MultipartFile file,
            @RequestParam("info") String videoInfo) {
        User user = (User) authentication.getPrincipal();

        UUID fileUuid = videoService.save(videoMetadataMapper.map(
                mapper.readValue(videoInfo, VideoInfo.class), user.id()), file);
        return ResponseEntity.ok(fileUuid);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<byte[]> fetchChunk(
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String range,
            @PathVariable UUID uuid
    ) {
        Range parsedRange = Range.parseHttpRangeString(range, defaultChunkSize);
        DefaultVideoService.ChunkWithMetadata chunkWithMetadata = videoService.fetchChunk(uuid, parsedRange);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(CONTENT_TYPE, chunkWithMetadata.metadata().getHttpContentType())
                .header(ACCEPT_RANGES, HttpConstants.ACCEPTS_RANGES_VALUE)
                .header(CONTENT_LENGTH, calculateContentLengthHeader(parsedRange, chunkWithMetadata.metadata().getSize()))
                .header(CONTENT_RANGE, constructContentRangeHeader(parsedRange, chunkWithMetadata.metadata().getSize()))
                .body(chunkWithMetadata.chunk());
    }

    private String calculateContentLengthHeader(Range range, long fileSize) {
        return String.valueOf(range.getRangeEnd(fileSize) - range.getRangeStart() + 1);
    }

    private String constructContentRangeHeader(Range range, long fileSize) {
        return "bytes " + range.getRangeStart() + "-" + range.getRangeEnd(fileSize) + "/" + fileSize;
    }
}
