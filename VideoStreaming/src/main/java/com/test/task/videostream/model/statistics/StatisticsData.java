package com.test.task.videostream.model.statistics;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StatisticsData {
    String userId;
    List<String> videoIds;
    String action;
}
