package com.rocketpt.server.controller.param;

import jakarta.validation.constraints.NotBlank;

public record LoginParam(@NotBlank String username,
                         String uuid,
                         String code,
                         @NotBlank String password) {
}
