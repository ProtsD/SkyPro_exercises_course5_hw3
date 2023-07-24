package ru.skypro.skypro_exercises_course5_hw3.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(this::customizeRequest);
        return http.build();
    }

    private void customizeRequest(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        try {
            registry
                    .requestMatchers(new AntPathRequestMatcher("/**", "POST"))
//                    .hasAnyRole("ADMIN")
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/**", "PUT"))
//                    .hasAnyRole("ADMIN")
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/**", "DELETE"))
//                    .hasAnyRole("ADMIN")
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/**", "GET"))
//                    .hasAnyRole("USER", "ADMIN")
                    .permitAll()
                    .and()
                    .formLogin().permitAll()
                    .and()
                    .logout().logoutUrl("/logout");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}