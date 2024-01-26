package com.test.task.videostream.model.api;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoInfo {
    String title;
    String director;
    String synopsis;
    String genre;
    Integer releaseYear;

    public VideoInfo(String title, String director, String synopsis, String genre, Integer releaseYear) {
        this.title = title;
        this.director = director;
        this.synopsis = synopsis;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }

    public VideoInfo() {
    }

    public String getTitle() {
        return this.title;
    }

    public String getDirector() {
        return this.director;
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public String getGenre() {
        return this.genre;
    }

    public Integer getReleaseYear() {
        return this.releaseYear;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
}
