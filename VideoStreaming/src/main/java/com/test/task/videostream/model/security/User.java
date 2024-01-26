package com.test.task.videostream.model.security;

import lombok.Builder;

@Builder
public record User(
        String id,
        String email,
        String firstName,
        String lastName
) {
}
