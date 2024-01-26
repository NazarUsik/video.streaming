package com.test.task.stats.data.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Action {
    LOAD_INFO("load-info"), PLAY_VIDEO("play-video");

    private final String action;

    public static Action fromString(String actionStr) {
        if (StringUtils.isBlank(actionStr)) {
            return null;
        }

        return Stream.of(Action.values())
                .filter(act -> act.getAction().equals(actionStr))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
