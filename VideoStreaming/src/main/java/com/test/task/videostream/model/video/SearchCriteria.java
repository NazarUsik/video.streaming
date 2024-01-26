package com.test.task.videostream.model.video;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchCriteria {
    String genre;
    String director;
    String keyword;
    Integer yearAfter;
    Integer year;
    Integer yearBefore;

    public boolean isEmpty() {
        return genre == null
                && director == null
                && keyword == null
                && yearAfter == null
                && yearBefore == null
                && year == null;
    }
}
