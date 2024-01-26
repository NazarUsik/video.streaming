package com.test.task.stats.service.message;

import com.test.task.stats.data.entity.Action;
import com.test.task.stats.data.entity.Statistic;
import com.test.task.stats.data.service.StatisticService;
import com.test.task.stats.model.message.StatisticsData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsListener {
    private final StatisticService statisticService;

    @KafkaListener(topics = "statistics", groupId = "1")
    public void saveStatistics(StatisticsData data) {
        data.getVideoIds().forEach(videoId ->
                statisticService.save(
                        Statistic.builder()
                                .userId(data.getUserId())
                                .videoId(videoId)
                                .action(Action.fromString(data.getAction()))
                                .build()));
    }
}
