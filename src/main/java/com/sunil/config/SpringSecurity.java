package com.sunil.config;

import com.sunil.JwtFilter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    // ✅ Constructor Injection
    public SpringSecurity(@Lazy UserDetailsService userDetailsService, @Lazy JwtFilter jwtFilter) {
	    this.userDetailsService = userDetailsService;
	    this.jwtFilter = jwtFilter;
	}


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    .cors() // ✅ THIS LINE IS MANDATORY
                .and()  // ✅ Go back to HttpSecurity
                .csrf(csrf -> csrf.disable()) // 🔒 CSRF disabled for APIs
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN") // 🔐 Admin access
                .requestMatchers("/journal/**", "/user/**").authenticated() // 🔐 JWT required
                .anyRequest().permitAll() // 🌐 Public endpoints allowed
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 🔑 JWT = Stateless
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // 📦 Add JWT filter

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
