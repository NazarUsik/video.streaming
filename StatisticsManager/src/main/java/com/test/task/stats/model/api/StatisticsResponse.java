package com.test.task.stats.model.api;

import lombok.Builder;

@Builder
public record StatisticsResponse(
        String videoId,
        Integer impressions,
        Integer views
) {
}
