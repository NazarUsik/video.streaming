package com.test.task.stats.model.message;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Objects;


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
