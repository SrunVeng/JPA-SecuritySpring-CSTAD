package com.example.mbaningapijpapractice.security;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(endpoint -> endpoint
                        .requestMatchers(HttpMethod.GET,"/v1/api/acc/**").hasAnyAuthority("SCOPE_ROLE_MANAGER","SCOPE_ROLE_ADMIN","SCOPE_ROLE_STAFF")
                        .requestMatchers(HttpMethod.POST, "/v1/api/user/create").hasAnyAuthority("SCOPE_ROLE_ADMIN", "SCOPE_ROLE_MANAGER","SCOPE_ROLE_STAFF")
                        .requestMatchers(HttpMethod.GET,"/v1/api/user").hasAnyAuthority("SCOPE_ROLE_ADMIN", "SCOPE_ROLE_MANAGER","SCOPE_ROLE_STAFF")
                        .requestMatchers(HttpMethod.GET,"/v1/api/user/id").hasAnyAuthority("SCOPE_ROLE_ADMIN", "SCOPE_ROLE_MANAGER","SCOPE_ROLE_STAFF")
                        .requestMatchers(HttpMethod.GET,"/v1/api/user/uuid").hasAnyAuthority("SCOPE_ROLE_ADMIN", "SCOPE_ROLE_MANAGER","SCOPE_ROLE_STAFF")
                        .requestMatchers(HttpMethod.GET,"/v1/api/user/create").hasAnyAuthority("SCOPE_ROLE_ADMIN", "SCOPE_ROLE_MANAGER","SCOPE_ROLE_STAFF")
                        .requestMatchers(HttpMethod.GET,"/v1/api/user/phoneNo").hasAnyAuthority("SCOPE_ROLE_ADMIN", "SCOPE_ROLE_MANAGER","SCOPE_ROLE_STAFF")
                        .requestMatchers(HttpMethod.GET,"/v1/api/user/resetPassword").permitAll()
                        .requestMatchers(HttpMethod.GET,"/v1/api/user/verifyResetPassword").permitAll()
                        .requestMatchers(HttpMethod.GET,"/v1/api/card-types").permitAll()
                        .requestMatchers(HttpMethod.GET,"/v1/api/account-types").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/file/**").permitAll()
                        .anyRequest().permitAll())
                //.httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    // work with database
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);
        // auth.authenticate()
        return auth;
    }

    // work with token
    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider(@Qualifier("jwtDecoderRefreshToken") JwtDecoder jwtDecoderRefreshToken) {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoderRefreshToken);
        provider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
        return provider;
    }


    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        //converter.setPrincipalClaimName("authority");
        return new JwtAuthenticationConverter();
    }


}
