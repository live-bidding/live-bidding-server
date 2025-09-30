package com.livebidding.server.user.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livebidding.server.auth.jwt.JwtTokenProvider;
import com.livebidding.server.global.config.SecurityConfig;
import com.livebidding.server.user.api.dto.request.LoginRequest;
import com.livebidding.server.user.api.dto.request.SignupRequest;
import com.livebidding.server.user.api.dto.response.TokenResponse;
import com.livebidding.server.user.application.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("/api/users/signup 요청 시")
    class Signup {
        @Test
        @DisplayName("성공하면 201 Created를 반환한다")
        void signup_success() throws Exception {
            // given
            SignupRequest request = new SignupRequest("test@test.com", "password123", "testuser");

            // when & then
            mockMvc.perform(post("/api/users/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andDo(print());
        }

        @Test
        @DisplayName("이메일이 비어있으면 400 Bad Request를 반환한다")
        void signup_fail_blank_email() throws Exception {
            // given
            SignupRequest request = new SignupRequest("", "password123", "testuser");

            // when & then
            mockMvc.perform(post("/api/users/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("/api/users/login 요청 시")
    class Login {
        @Test
        @DisplayName("성공하면 200 OK와 함께 토큰을 반환한다")
        void login_success() throws Exception {
            // given
            LoginRequest request = new LoginRequest("test@test.com", "password123");
            TokenResponse tokenResponse = new TokenResponse("accessToken", "refreshToken");
            given(userService.login(any(LoginRequest.class))).willReturn(tokenResponse);

            // when & then
            mockMvc.perform(post("/api/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("accessToken"))
                    .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                    .andDo(print());
        }
    }
}
