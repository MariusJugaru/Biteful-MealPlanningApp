package com.biteful.mealplanner.userservice.controllers;

import com.biteful.mealplanner.userservice.TestDataUtil;
import com.biteful.mealplanner.userservice.domain.dto.LoginRequestDto;
import com.biteful.mealplanner.userservice.domain.dto.LoginResponseDto;
import com.biteful.mealplanner.userservice.domain.dto.UserCreateRequestDto;
import com.biteful.mealplanner.userservice.exceptions.runtime.EmailAlreadyExistsException;
import com.biteful.mealplanner.userservice.exceptions.runtime.InvalidCredentialsException;
import com.biteful.mealplanner.userservice.services.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthControllerTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final JwtService jwtService;

    @Autowired
    public AuthControllerTests(MockMvc mockMvc, JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.jwtService = jwtService;
    }

    @Test
    public void testThatUserCanBeRegisteredAndReturns201Created() throws Exception {
        UserCreateRequestDto requestDto = TestDataUtil.createRequestDtoA();

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

        String response = result.getResponse().getContentAsString();

        LoginResponseDto responseDto = objectMapper.readValue(response, LoginResponseDto.class);

        // Check if a token is returned
        assertThat(responseDto.getToken()).isNotNull();

        // Verify token claims
        Claims claims = jwtService.parseJwt(responseDto.getToken());
        assertThat(claims.get("username")).isEqualTo(responseDto.getUsername());
        assertThat(claims.get("role")).isEqualTo("USER");
    }

    @Test
    public void testThatEmailCantBeUsedTwice() throws Exception {
        UserCreateRequestDto requestDto = TestDataUtil.createRequestDtoA();


        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        )
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andReturn();

        String response = result.getResponse().getContentAsString();

        Map<String, Object> body = objectMapper.readValue(response, Map.class);

        // Check if the error message is returned
        assertThat(body.get("message")).isEqualTo(new EmailAlreadyExistsException().getMessage());
    }

    @Test
    public void testThatUserCanLoginWithUsername() throws Exception {
        UserCreateRequestDto requestDto = TestDataUtil.createRequestDtoA();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .usernameOrEmail(requestDto.getUsername())
                .password(requestDto.getPassword())
                .build();

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();

        LoginResponseDto responseDto = objectMapper.readValue(response, LoginResponseDto.class);

        // Check if a token is returned
        assertThat(responseDto.getToken()).isNotNull();

        // Verify token claims
        Claims claims = jwtService.parseJwt(responseDto.getToken());
        assertThat(claims.get("username")).isEqualTo(responseDto.getUsername());
        assertThat(claims.get("role")).isEqualTo("USER");
    }

    @Test
    public void testThatUserCanLoginWithEmail() throws Exception {
        UserCreateRequestDto requestDto = TestDataUtil.createRequestDtoA();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .usernameOrEmail(requestDto.getEmail())
                .password(requestDto.getPassword())
                .build();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        LoginResponseDto responseDto = objectMapper.readValue(response, LoginResponseDto.class);

        // Check if a token is returned
        assertThat(responseDto.getToken()).isNotNull();

        // Verify token claims
        Claims claims = jwtService.parseJwt(responseDto.getToken());
        assertThat(claims.get("username")).isEqualTo(responseDto.getUsername());
        assertThat(claims.get("role")).isEqualTo("USER");
    }

    @Test
    public void testThatUserCantLoginWithInvalidCredentials() throws Exception {
        UserCreateRequestDto requestDto = TestDataUtil.createRequestDtoA();

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .usernameOrEmail(requestDto.getEmail())
                .password(requestDto.getPassword())
                .build();

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        )
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andReturn();

        String response = result.getResponse().getContentAsString();

        Map<String, Object> body = objectMapper.readValue(response, Map.class);

        // Check if the error message is returned
        assertThat(body.get("message")).isEqualTo(new InvalidCredentialsException().getMessage());
    }
}
