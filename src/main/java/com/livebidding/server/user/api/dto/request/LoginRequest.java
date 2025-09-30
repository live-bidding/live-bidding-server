package com.livebidding.server.user.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        String email,
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        String password
) {}
