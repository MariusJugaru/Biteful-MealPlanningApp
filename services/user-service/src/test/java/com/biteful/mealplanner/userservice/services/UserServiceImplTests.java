package com.biteful.mealplanner.userservice.services;

import com.biteful.mealplanner.userservice.TestDataUtil;
import com.biteful.mealplanner.userservice.domain.dto.LoginRequestDto;
import com.biteful.mealplanner.userservice.domain.dto.UpdatePasswordRequestDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceImplTests {

    private final UserService underTest;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserServiceImplTests(UserService underTest, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.underTest = underTest;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Test
    public void testThatUserCanBeCreatedAndRecalledById() {
        UserEntity userEntityA = TestDataUtil.createUserEnityA(passwordEncoder);
        UserEntity userSavedEntityA = underTest.createUser(userEntityA);

        Optional<UserEntity> result = underTest.getUserByID(userSavedEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get())
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(userSavedEntityA);
    }

    @Test
    public void testThatUserCanBeCreatedAndRecalledByUsername() {
        UserEntity userEntityA = TestDataUtil.createUserEnityA(passwordEncoder);
        UserEntity userSavedEntityA = underTest.createUser(userEntityA);

        Optional<UserEntity> result = underTest.getUserByUsername(userSavedEntityA.getUsername());
        assertThat(result).isPresent();
        assertThat(result.get())
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(userSavedEntityA);
    }

    @Test
    public void testThatUserCanBeCreatedAndRecalledByEmail() {
        UserEntity userEntityA = TestDataUtil.createUserEnityA(passwordEncoder);
        UserEntity userSavedEntityA = underTest.createUser(userEntityA);

        Optional<UserEntity> result = underTest.getUserByEmail(userSavedEntityA.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get())
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(userSavedEntityA);
    }

    @Test
    public void testThatMultipleUsersCanBeCreatedAndRecalled() {
        UserEntity userEntityA = TestDataUtil.createUserEnityA(passwordEncoder);
        UserEntity userSavedEntityA = underTest.createUser(userEntityA);

        UserEntity userEntityB = TestDataUtil.createUserEnityB(passwordEncoder);
        UserEntity userSavedEntityB = underTest.createUser(userEntityB);

        Pageable pageable = PageRequest.of(0, 10);
        List<UserEntity> result = underTest.getAllUsers(pageable).getContent();
        assertThat(result).hasSize(2)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt")
                .containsExactly(userSavedEntityA, userSavedEntityB);
    }

    @Test
    public void testThatUserCanChangePassword() {
        UserEntity userEntityA = TestDataUtil.createUserEnityA(passwordEncoder);
        UserEntity userSavedEntityA = underTest.createUser(userEntityA);

        UpdatePasswordRequestDto updatePasswordRequestDto = UpdatePasswordRequestDto.builder()
                .oldPassword("123456")
                .newPassword("123")
                .build();

        UserEntity userUpdated = underTest.updateUserPassword(userSavedEntityA.getId(), updatePasswordRequestDto);
        assertThat(userUpdated).isNotNull();
        assertThat(passwordEncoder.matches("123", userUpdated.getPasswordHash()));
    }

    @Test
    public void testThatThrowsExceptionWhenOldPasswordIsWrong() {
        UserEntity userEntityA = TestDataUtil.createUserEnityA(passwordEncoder);
        UserEntity userSavedEntityA = underTest.createUser(userEntityA);

        UpdatePasswordRequestDto updatePasswordRequestDto = UpdatePasswordRequestDto.builder()
                .oldPassword("12345")
                .newPassword("123")
                .build();

        try {
            underTest.updateUserPassword(userSavedEntityA.getId(), updatePasswordRequestDto);
            assert false;
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Old password is incorrect");
        }
    }

//    @Test
//    public void testThatUserCanLogIn() {
//        UserEntity userEntityA = TestDataUtil.createUserEnityA(passwordEncoder);
//        UserEntity userSavedEntityA = underTest.createUser(userEntityA);
//
//        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
//                .usernameOrEmail(userSavedEntityA.getUsername())
//                .password("123456")
//                .build();
//
//        try {
//            String jwt = underTest.loginUser(loginRequestDto);
//            System.out.println("TOKEN");
//            System.out.println(jwt);
//            assertThat(jwtService.parseJwt(jwt).getSubject()).isEqualTo(userSavedEntityA.getId().toString());
//
//        } catch (RuntimeException e) {}
//    }


}
