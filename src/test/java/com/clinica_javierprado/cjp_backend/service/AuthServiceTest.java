package com.clinica_javierprado.cjp_backend.service;

import com.clinica_javierprado.cjp_backend.domain.EmailVerificationToken;
import com.clinica_javierprado.cjp_backend.domain.Role;
import com.clinica_javierprado.cjp_backend.domain.User;
import com.clinica_javierprado.cjp_backend.dto.AuthResponse;
import com.clinica_javierprado.cjp_backend.dto.LoginRequest;
import com.clinica_javierprado.cjp_backend.dto.RegisterRequest;
import com.clinica_javierprado.cjp_backend.exception.EmailNotVerifiedException;
import com.clinica_javierprado.cjp_backend.repository.EmailVerificationTokenRepository;
import com.clinica_javierprado.cjp_backend.repository.PasswordResetTokenRepository;
import com.clinica_javierprado.cjp_backend.repository.UserRepository;
import com.clinica_javierprado.cjp_backend.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtService jwtService = mock(JwtService.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final PasswordResetTokenRepository passwordResetTokenRepository = mock(PasswordResetTokenRepository.class);
    private final EmailVerificationTokenRepository emailVerificationTokenRepository = mock(EmailVerificationTokenRepository.class);
    private final EmailService emailService = mock(EmailService.class);

    private final AuthService authService = new AuthService(
            userRepository,
            passwordEncoder,
            jwtService,
            authenticationManager,
            passwordResetTokenRepository,
            emailVerificationTokenRepository,
            emailService
    );

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "emailVerificationCodeExpirationMinutes", 15L);
        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> "encoded-" + invocation.getArgument(0, String.class));
    }

    @Test
    void registerCreatesUnverifiedUserAndSendsVerificationCode() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Ana");
        request.setLastName("Paz");
        request.setDni(12345678);
        request.setEmail("ana@test.com");
        request.setPassword("password123");
        request.setPhoneNumber("987654321");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0, User.class);
            user.setId(1L);
            return user;
        });

        authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmailVerified()).isFalse();
        assertThat(savedUser.getRole()).isEqualTo(Role.PATIENT);
        assertThat(savedUser.getPassword()).isEqualTo("encoded-password123");

        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).sendVerificationCodeEmail(eq("ana@test.com"), eq("Ana"), codeCaptor.capture(), eq(15L));
        assertThat(codeCaptor.getValue()).matches("\\d{6}");

        ArgumentCaptor<EmailVerificationToken> tokenCaptor = ArgumentCaptor.forClass(EmailVerificationToken.class);
        verify(emailVerificationTokenRepository).save(tokenCaptor.capture());
        assertThat(tokenCaptor.getValue().getCodeHash()).isEqualTo("encoded-" + codeCaptor.getValue());
        assertThat(tokenCaptor.getValue().getUser()).isSameAs(savedUser);
    }

    @Test
    void loginRejectsUnverifiedUserAfterValidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ana@test.com");
        request.setPassword("password123");

        User user = User.builder()
                .email("ana@test.com")
                .emailVerified(false)
                .role(Role.PATIENT)
                .password("encoded-password")
                .build();
        when(userRepository.findByEmail("ana@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encoded-password")).thenReturn(true);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(EmailNotVerifiedException.class)
                .hasMessage("Debes verificar tu correo antes de iniciar sesion.");

        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    void loginRejectsInvalidPasswordBeforeEmailVerificationState() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ana@test.com");
        request.setPassword("wrong-password");

        User user = User.builder()
                .email("ana@test.com")
                .emailVerified(false)
                .role(Role.PATIENT)
                .password("encoded-password")
                .build();
        when(userRepository.findByEmail("ana@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email or password");

        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    void loginRejectsUserWithUnknownVerificationState() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ana@test.com");
        request.setPassword("password123");

        User user = User.builder()
                .email("ana@test.com")
                .emailVerified(null)
                .role(Role.PATIENT)
                .password("encoded-password")
                .build();
        when(userRepository.findByEmail("ana@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encoded-password")).thenReturn(true);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(EmailNotVerifiedException.class)
                .hasMessage("Debes verificar tu correo antes de iniciar sesion.");

        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    void userIsEnabledOnlyWhenEmailIsVerified() {
        assertThat(User.builder().emailVerified(true).build().isEnabled()).isTrue();
        assertThat(User.builder().emailVerified(false).build().isEnabled()).isFalse();
        assertThat(User.builder().emailVerified(null).build().isEnabled()).isFalse();
    }

    @Test
    void verifyEmailMarksUserAsVerifiedAndSendsWelcomeEmail() {
        User user = User.builder()
                .id(1L)
                .firstName("Ana")
                .email("ana@test.com")
                .emailVerified(false)
                .role(Role.PATIENT)
                .password("encoded-password")
                .build();
        EmailVerificationToken token = EmailVerificationToken.builder()
                .user(user)
                .codeHash("hash")
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail("ana@test.com")).thenReturn(Optional.of(user));
        when(emailVerificationTokenRepository.findByUserId(1L)).thenReturn(Optional.of(token));
        when(passwordEncoder.matches("123456", "hash")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.verifyEmail("ana@test.com", "123456");

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getMessage()).isEqualTo("Correo verificado correctamente.");
        assertThat(user.getEmailVerified()).isTrue();
        verify(userRepository).save(user);
        verify(emailVerificationTokenRepository).delete(token);
        verify(emailService).sendWelcomeEmail("ana@test.com", "Ana");
    }
}
