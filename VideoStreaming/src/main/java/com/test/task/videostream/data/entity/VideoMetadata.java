package com.test.task.videostream.data.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoMetadata {
    @Id
    String id;
    long size;
    String httpContentType;

    String authorId;
    String title;
    @Nullable
    String director;
    @Nullable
    String synopsis;
    @Nullable
    String genre;
    @Nullable
    Integer releaseYear;

    Boolean active;
}
