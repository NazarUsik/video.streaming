package com.test.task.videostream.controller;

import com.test.task.videostream.data.entity.VideoMetadata;
import com.test.task.videostream.model.security.User;
import com.test.task.videostream.model.video.SearchCriteria;
import com.test.task.videostream.data.service.VideoMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/metadata")
public class VideoMetadataController {
    private final VideoMetadataService videoMetadataService;

    @GetMapping("/search")
    public ResponseEntity<List<VideoMetadata>> search(
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer yearBefore,
            @RequestParam(required = false) Integer yearAfter,
            @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok()
                .body(videoMetadataService.search(
                        SearchCriteria.builder()
                                .director(director)
                                .keyword(keyword)
                                .genre(genre)
                                .yearBefore(yearBefore)
                                .yearAfter(yearAfter)
                                .year(year)
                                .build()
                ));
    }

    @GetMapping("/all")
    public ResponseEntity<List<VideoMetadata>> all() {
        return ResponseEntity.ok().body(videoMetadataService.all());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<VideoMetadata>> deleted() {
        return ResponseEntity.ok().body(videoMetadataService.deleted());
    }

    @GetMapping("/author")
    public ResponseEntity<List<VideoMetadata>> author(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok().body(videoMetadataService.byAuthorId(user.id()));
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<List<VideoMetadata>> byAuthorId(@PathVariable String authorId) {
        return ResponseEntity.ok().body(videoMetadataService.byAuthorId(authorId));
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<List<VideoMetadata>> delete(@PathVariable String videoId) {
        videoMetadataService.remove(videoId);
        return ResponseEntity.ok().build();
    }
}
