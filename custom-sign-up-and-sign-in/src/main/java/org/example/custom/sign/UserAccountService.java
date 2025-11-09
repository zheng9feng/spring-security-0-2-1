package org.example.custom.sign;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
class UserAccountService {

    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    UserAccountService(UserAccountRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    UserAccount register(String email, String rawPassword) {
        repository.findByEmailIgnoreCase(email)
                .ifPresent(account -> {
                    throw new IllegalArgumentException("Email already registered");
                });
        String encodedPassword = passwordEncoder.encode(rawPassword);
        UserAccount toSave = new UserAccount(email, encodedPassword);

        return repository.save(toSave);
    }
}
