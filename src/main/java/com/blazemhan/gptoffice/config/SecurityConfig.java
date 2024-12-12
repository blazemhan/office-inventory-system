package com.blazemhan.gptoffice.config;

import com.blazemhan.gptoffice.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final CustomUserDetailsService userDetailsService;
        public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
            this.userDetailsService = customUserDetailsService;
        }

        @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                    http.csrf(AbstractHttpConfigurer::disable)
                            .authorizeHttpRequests(auth -> auth
                                    .requestMatchers("/api/auth/**").permitAll()
                                    .requestMatchers("/api/supplies/request/**").hasAuthority("USER")
                                    .requestMatchers("/api/supplies/approve/**").hasAuthority("MANAGER")
                                    .requestMatchers("/api/supplies/reject/**").hasAuthority("MANAGER")
                                    .requestMatchers("/api/items/add/**").hasAuthority("ADMIN")
                                    .anyRequest().authenticated()
                            )

                            .httpBasic(Customizer.withDefaults());

            return http.build();
        }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();

    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.authenticationProvider(daoAuthProvider());
        return auth.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
