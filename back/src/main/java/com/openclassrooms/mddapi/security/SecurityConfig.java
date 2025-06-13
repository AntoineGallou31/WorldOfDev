// Java
package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // JWT filter to validate JWT tokens in requests
    private final JwtAuthenticationFilter jwtAuthFilter;

    // Configures the security filter chain for HTTP requests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection as it's not needed for a REST API
                .authorizeHttpRequests(auth -> auth
                        // Allows access to authentication endpoints without a JWT
                        .requestMatchers("/api/auth/**").permitAll()
                        // Allows access to image endpoints without a JWT
                        .requestMatchers("/api/images/**").permitAll()
                        // Allows access to Swagger UI and API docs
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                // Sets session management to stateless as no server-side session is maintained
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Adds the JWT filter before the UsernamePasswordAuthenticationFilter in the filter chain
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Builds and returns the SecurityFilterChain instance
        return http.build();
    }

    // Configures the password encoder bean using BCrypt algorithm
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt provides a secure hashing algorithm for passwords
    }

    // Configures CORS settings for the application
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Sets the allowed origins. Replace with the URL of your frontend.
        configuration.setAllowedOrigins(Arrays.asList("http://votre-frontend.com"));
        // Sets the allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Sets the allowed headers
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);

        // Maps the configuration to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        // Returns the CORS configuration source
        return source;
    }
}