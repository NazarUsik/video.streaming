package com.test.task.videostream.service.statistics;

import com.test.task.videostream.model.statistics.StatisticsData;
import com.test.task.videostream.service.message.MessageManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.test.task.videostream.config.AsyncConfig.ASYNC_TASK_EXECUTOR;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final MessageManager msgManager;

    @SneakyThrows
    @Async(ASYNC_TASK_EXECUTOR)
    public void sendStatistics(StatisticsData statistics) {
        msgManager.sendMessage(statistics);
    }
}
