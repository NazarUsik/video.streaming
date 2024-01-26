package com.test.task.stats.controller;

import com.test.task.stats.data.service.StatisticService;
import com.test.task.stats.model.api.StatisticsResponse;
import com.test.task.stats.model.security.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/total")
    public ResponseEntity<List<StatisticsResponse>> total() {
        return ResponseEntity.ok().body(statisticService.totalStatistics());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clear() {
        statisticService.clear();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/author")
    public ResponseEntity<List<StatisticsResponse>> author(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok().body(statisticService.statisticsByUser(user.id()));
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<StatisticsResponse>> byUser(@PathVariable String userId) {
        return ResponseEntity.ok().body(statisticService.statisticsByUser(userId));
    }

    @GetMapping("/byVideo/{videoId}")
    public ResponseEntity<StatisticsResponse> byVideo(@PathVariable String videoId) {
        return ResponseEntity.ok().body(statisticService.statisticsByVideo(videoId));
    }
}
