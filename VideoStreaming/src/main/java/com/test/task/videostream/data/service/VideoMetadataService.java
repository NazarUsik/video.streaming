package com.test.task.videostream.data.service;

import com.test.task.videostream.data.entity.VideoMetadata;
import com.test.task.videostream.model.security.User;
import com.test.task.videostream.model.statistics.StatisticsData;
import com.test.task.videostream.model.video.SearchCriteria;
import com.test.task.videostream.data.repository.VideoMetadataRepository;
import com.test.task.videostream.service.statistics.StatisticsService;
import com.test.task.videostream.util.CriteriaPredicateBuilderHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoMetadataService {
    private final VideoMetadataRepository videoMDRepository;
    private final StatisticsService statisticsService;
    private final EntityManager em;
    private final CriteriaPredicateBuilderHelper criteriaHelper;

    @CachePut("VideoMetadata")
    public UUID save(VideoMetadata metadata) {
        UUID uuid = UUID.randomUUID();
        metadata.setId(uuid.toString());
        metadata.setActive(true);

        videoMDRepository.save(metadata);
        return uuid;
    }

    @CacheEvict("VideoMetadata")
    public void remove(String id) {
        videoMDRepository.findById(id).ifPresent(metadata -> {
            metadata.setActive(false);
            videoMDRepository.save(metadata);
        });
    }

    @Cacheable("VideoMetadata")
    public Optional<VideoMetadata> byId(String id) {
        Optional<VideoMetadata> video = videoMDRepository.findById(id);

        sendStatistics(video);

        return video;
    }

    @Cacheable("VideoMetadata")
    public List<VideoMetadata> all() {
        List<VideoMetadata> videos = videoMDRepository.findAllByActiveIsTrue();

        sendStatistics(videos);

        return videos;
    }

    @Cacheable("VideoMetadata")
    public List<VideoMetadata> deleted() {
        List<VideoMetadata> videos = videoMDRepository.findAllByActiveIsFalse();

        sendStatistics(videos);

        return videos;
    }

    @Cacheable("VideoMetadata")
    public List<VideoMetadata> byAuthorId(String authorId) {
        List<VideoMetadata> videos = videoMDRepository.findAllByAuthorIdAndActiveIsTrue(authorId);

        sendStatistics(videos);

        return videos;
    }

    @Cacheable("VideoMetadata")
    public List<VideoMetadata> search(SearchCriteria criteria) {
        if (criteria.isEmpty()) {
            return this.all();
        }

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<VideoMetadata> query = criteriaBuilder.createQuery(VideoMetadata.class);
        Root<VideoMetadata> videoMD = query.from(VideoMetadata.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(criteria.getKeyword())) {
            predicates.add(criteriaHelper.like(criteriaBuilder, videoMD, "title", criteria.getKeyword()));
            predicates.add(criteriaHelper.like(criteriaBuilder, videoMD, "synopsis", criteria.getKeyword()));
        }
        if (StringUtils.isNotBlank(criteria.getKeyword())) {
            predicates.add(criteriaHelper.equal(criteriaBuilder, videoMD, "director", criteria.getDirector()));
        }
        if (StringUtils.isNotBlank(criteria.getKeyword())) {
            predicates.add(criteriaHelper.equal(criteriaBuilder, videoMD, "genre", criteria.getGenre()));
        }
        if (criteria.getYearAfter() != null || criteria.getYearBefore() != null || criteria.getYear() != null) {
            predicates.add(criteriaHelper.betweenOrEqual(criteriaBuilder, videoMD, "releaseYear",
                    criteria.getYearAfter(), criteria.getYearBefore(), criteria.getYear()));
        }

        query.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

        List<VideoMetadata> videos = em.createQuery(query).getResultList();

        sendStatistics(videos);

        return videos;
    }

    private void sendStatistics(Optional<VideoMetadata> optional) {
        optional.ifPresent(this::sendStatistics);
    }

    private void sendStatistics(VideoMetadata video) {
        sendStatistics(Collections.singletonList(video));
    }

    private void sendStatistics(List<VideoMetadata> videos) {
        if (!videos.isEmpty()) {
            statisticsService.sendStatistics(buildStatisticsData(videoIds(videos)));
        }
    }

    private List<String> videoIds(List<VideoMetadata> videos) {
        return videos.stream()
                .map(VideoMetadata::getId)
                .collect(Collectors.toList());
    }

    private StatisticsData buildStatisticsData(List<String> videoIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return StatisticsData.builder()
                .userId(user.id())
                .videoIds(videoIds)
                .action("load-info")
                .build();
    }
}
