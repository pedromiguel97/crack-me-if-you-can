package io.github.pedromiguel97.crackme.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyTakenException(username);
        }
        String passwordHash = passwordEncoder.encode(rawPassword);
        return userRepository.save(new User(username, passwordHash));
    }
}