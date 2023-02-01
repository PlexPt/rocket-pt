package com.rocketpt.server.dto.param;

import jakarta.validation.constraints.NotBlank;

public record LoginParam(@NotBlank String username,
                         String uuid,
                         String code,
                         @NotBlank String password) {
}
