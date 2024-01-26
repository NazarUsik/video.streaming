package com.test.task.stats.model.security;

import lombok.Builder;

@Builder
public record User(
        String id,
        String email,
        String firstName,
        String lastName
) {
}
