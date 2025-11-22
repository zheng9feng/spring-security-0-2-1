package org.example.retrieve.user.from.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserManagementConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // alternatively, you can customize DelegatingPasswordEncoder by yourself to support target encodings
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
