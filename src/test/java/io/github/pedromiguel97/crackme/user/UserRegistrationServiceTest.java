package io.github.pedromiguel97.crackme.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserRegistrationService service;

    @BeforeEach
    void setUp() {
        service = new UserRegistrationService(userRepository, passwordEncoder);
    }

    @Test
    void storesHashInsteadOfRawPassword() {
        // given
        when(userRepository.existsByUsername("dave")).thenReturn(false);
        when(passwordEncoder.encode("s3cret!")).thenReturn("hashed-s3cret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User registered = service.register("dave", "s3cret!");

        // then
        ArgumentCaptor<User> savedUser = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(savedUser.capture());
        assertThat(savedUser.getValue().getUsername()).isEqualTo("dave");
        assertThat(savedUser.getValue().getPasswordHash())
                .isEqualTo("hashed-s3cret")
                .isNotEqualTo("s3cret!");
        assertThat(registered).isSameAs(savedUser.getValue());
    }

    @Test
    void rejectsUsernameThatIsAlreadyTaken() {
        // given
        when(userRepository.existsByUsername("dave")).thenReturn(true);

        // when / then
        assertThatThrownBy(() -> service.register("dave", "s3cret!"))
                .isInstanceOf(UsernameAlreadyTakenException.class)
                .hasMessageContaining("dave");

        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }
}