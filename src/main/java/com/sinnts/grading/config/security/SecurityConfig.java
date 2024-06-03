package com.sinnts.grading.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.sinnts.grading.user.enums.Permission.*;
import static com.sinnts.grading.user.enums.Role.ADMIN;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
//  private final LogoutHandler logoutHandler;

  private static final String[] WHITE_LIST = {
      "/auth/**",
      "/v2/api-docs",
      "/v3/api-docs",
      "/v3/api-docs/**",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui/**",
      "/webjars/**",
      "/swagger-ui.html",
      "/**"
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            request -> request
                .requestMatchers(
                    WHITE_LIST
                ).permitAll()

                .requestMatchers("/departments").hasRole(ADMIN.name())
                .requestMatchers(GET, "/departments/**").hasAnyAuthority(ADMIN_READ.name())
                .requestMatchers(POST, "/departments/**").hasAnyAuthority(ADMIN_CREATE.name())
                .requestMatchers(PUT, "/departments/**").hasAnyAuthority(ADMIN_UPDATE.name())
                .requestMatchers(DELETE, "/departments/**").hasAnyAuthority(ADMIN_DELETE.name())

                .requestMatchers("/staffs").hasRole(ADMIN.name())
                .requestMatchers(GET, "/staffs/**").hasAnyAuthority(ADMIN_READ.name())
                .requestMatchers(POST, "/staffs/**").hasAnyAuthority(ADMIN_CREATE.name())
                .requestMatchers(PUT, "/staffs/**").hasAnyAuthority(ADMIN_UPDATE.name())
                .requestMatchers(DELETE, "/staffs/**").hasAnyAuthority(ADMIN_DELETE.name())

                .requestMatchers("/performances").hasRole(ADMIN.name())
                .requestMatchers(GET, "/performances/**").hasAnyAuthority(ADMIN_READ.name())
                .requestMatchers(POST, "/performances/**").hasAnyAuthority(ADMIN_CREATE.name())
                .requestMatchers(PUT, "/performances/**").hasAnyAuthority(ADMIN_UPDATE.name())
                .requestMatchers(DELETE, "/performances/**").hasAnyAuthority(ADMIN_DELETE.name())

                .requestMatchers("/gradings").hasRole(ADMIN.name())
                .requestMatchers(GET, "/gradings/**").hasAnyAuthority(ADMIN_READ.name())
                .requestMatchers(POST, "/gradings/**").hasAnyAuthority(ADMIN_CREATE.name())
                .requestMatchers(PUT, "/gradings/**").hasAnyAuthority(ADMIN_UPDATE.name())
                .requestMatchers(DELETE, "/gradings/**").hasAnyAuthority(ADMIN_DELETE.name())

                .anyRequest()
                .authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        )
        .logout(
            logout ->
//                logout.addLogoutHandler(logoutHandler)
                logout
                    .logoutUrl("/auth/logout")
                    .logoutSuccessHandler(
                        (request, response, authentication) -> SecurityContextHolder.clearContext()
                    )
        );

    return http.build();
  }
}
