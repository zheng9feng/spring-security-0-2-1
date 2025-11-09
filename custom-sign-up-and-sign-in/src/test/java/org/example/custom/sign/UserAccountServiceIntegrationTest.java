package org.example.custom.sign;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserAccountServiceIntegrationTest {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registersAndPersistsUserInH2() {
        String email = "integration-" + UUID.randomUUID() + "@example.com";
        String rawPassword = "Password123";

        userAccountService.register(email, rawPassword);

        UserAccount saved = userAccountRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AssertionError("User not persisted"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualToIgnoringCase(email);
        assertThat(passwordEncoder.matches(rawPassword, saved.getPassword())).isTrue();
    }
}
