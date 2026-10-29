package io.github.pedromiguel97.crackme.user;

import io.github.pedromiguel97.crackme.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    void savesUserAndFindsItByUsername() {
        userRepository.saveAndFlush(new User("alice", "not-a-real-hash"));

        assertThat(userRepository.findByUsername("alice"))
                .hasValueSatisfying(user -> {
                    assertThat(user.getId()).isNotNull();
                    assertThat(user.getCreatedAt()).isNotNull();
                });
    }

    @Test
    void reportsWhetherUsernameExists() {
        userRepository.saveAndFlush(new User("bob", "not-a-real-hash"));

        assertThat(userRepository.existsByUsername("bob")).isTrue();
        assertThat(userRepository.existsByUsername("carol")).isFalse();
    }

    @Test
    void rejectsDuplicateUsername() {
        userRepository.saveAndFlush(new User("dave", "not-a-real-hash"));

        assertThatThrownBy(() -> userRepository.saveAndFlush(new User("dave", "another-hash")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}