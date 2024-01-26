package com.test.task.stats.data.repository;

import com.test.task.stats.data.entity.Action;
import com.test.task.stats.data.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic, String> {
    List<Statistic> getAllByVideoIdAndAction(String videoId, Action action);

    @Query("""
            SELECT s.videoId
            FROM Statistic AS s
            GROUP BY s.videoId
            ORDER BY s.videoId""")
    List<String> getAllVideoIds();

    @Query("""
            SELECT s.videoId
            FROM Statistic AS s
            WHERE s.userId = :userId
            GROUP BY s.videoId
            ORDER BY s.videoId""")
    List<String> getAllVideoIdsByUserId(@Param("userId") String userId);
}
