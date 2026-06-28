package br.com.mybar.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Class responsible for system security configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Main security configuration
        http.csrf(AbstractHttpConfigurer::disable) // Disable protection on list endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/docs/**", // Disable protection
                                "/api-docs/**", // Disable protection
                                "/swagger-ui/**",  // Disable protection
                                "/swagger-ui.html", // Disable protection
                                "/client/**", // Disable protection
                                "/clients/**" // Disable protection
                        ).permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}

