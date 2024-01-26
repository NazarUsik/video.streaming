package com.test.task.stats.data.service;

import com.test.task.stats.data.entity.Action;
import com.test.task.stats.data.entity.Statistic;
import com.test.task.stats.data.repository.StatisticRepository;
import com.test.task.stats.model.api.StatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticRepository repository;

    public void clear() {
        repository.deleteAll();
    }

    public UUID save(Statistic statistic) {
        UUID uuid = UUID.randomUUID();
        statistic.setId(uuid.toString());

        repository.save(statistic);
        return uuid;
    }

    public StatisticsResponse statisticsByVideo(String videoId) {
        List<Statistic> impressions = repository.getAllByVideoIdAndAction(videoId, Action.LOAD_INFO);
        List<Statistic> views = repository.getAllByVideoIdAndAction(videoId, Action.PLAY_VIDEO);

        return StatisticsResponse.builder()
                .videoId(videoId)
                .impressions(impressions.size())
                .views(views.size())
                .build();
    }

    public List<StatisticsResponse> statisticsByUser(String userId) {
        List<String> videoIds = repository.getAllVideoIdsByUserId(userId);

        return videoIds.stream().map(this::statisticsByVideo)
                .collect(Collectors.toList());
    }

    public List<StatisticsResponse> totalStatistics() {
        List<String> videoIds = repository.getAllVideoIds();

        return videoIds.stream().map(this::statisticsByVideo)
                .collect(Collectors.toList());
    }
}
