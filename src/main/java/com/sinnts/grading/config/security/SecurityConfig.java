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
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.sinnts.grading.user.enums.Permission.*;
import static com.sinnts.grading.user.enums.Role.ADMIN;
import static com.sinnts.grading.user.enums.Role.SUPER_ADMIN;
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
  private final LogoutHandler logoutHandler;

  /**
   * These are the list of path that will be allowed to access without authentication, <b>check Line 68, the permitAll()</b> will make any URL form the WHITE_LIST accessible without authentication <br>
   * As the name WHITE_LIST says, can be accessed publicly, the <b>auth</b> is for authentication which it not suppose tobe secured <br>
   * And the remaining are for OpenAPI (Swagger) Documentations
   */
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
      "/swagger-ui.html"
  };

  /**
   * Both SUPER_ADMIN and ADMIN can access the <b>users</b> endpoints <br>
   * But Only the SUPER_ADMIN can perform all the [ POST, GET, UPDATE and DELETE ] request on the <b>users</b> endpoints <br>
   * The Normal ADMIN can only perform a Read-Only [ GET ] request on the <b>users</b> endpoints <br>
   * Check the Role and Permission Based Authentication section
   */
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

                // Role Based Authorization [ Users Endpoints ]
                .requestMatchers("/users/**").hasAnyRole(
                    SUPER_ADMIN.name(),
                    ADMIN.name()
                )

                // Permission Based Authorization [ Users Endpoints ]
                .requestMatchers(POST, "/users/**").hasAuthority(SUPER_ADMIN_CREATE.name())
                .requestMatchers(GET, "/users/**").hasAnyAuthority(SUPER_ADMIN_READ.name(), ADMIN_READ.name())
                .requestMatchers(PUT, "/users/**").hasAnyAuthority(SUPER_ADMIN_UPDATE.name())
                .requestMatchers(DELETE, "/users/**").hasAuthority(SUPER_ADMIN_DELETE.name())

                // Role Based Authorization [ Departments Endpoints ]
                .requestMatchers("/departments/**").hasAnyRole(
                    SUPER_ADMIN.name(),
                    ADMIN.name()
                )
                // Permission Based Authorization [ Departments Endpoints ]
                .requestMatchers(POST, "/departments/**").hasAnyAuthority(SUPER_ADMIN_CREATE.name(), ADMIN_CREATE.name())
                .requestMatchers(GET, "/departments/**").hasAnyAuthority(SUPER_ADMIN_READ.name(), ADMIN_READ.name())
                .requestMatchers(PUT, "/departments/**").hasAnyAuthority(SUPER_ADMIN_UPDATE.name(), ADMIN_UPDATE.name())
                .requestMatchers(DELETE, "/departments/**").hasAnyAuthority(SUPER_ADMIN_DELETE.name(), ADMIN_DELETE.name())

                // Role Based Authorization [ Staffs Endpoints ]
                .requestMatchers("/staffs/**").hasAnyRole(
                    SUPER_ADMIN.name(),
                    ADMIN.name()
                )
                // Permission Based Authorization [ Departments Endpoints ]
                .requestMatchers(POST, "/staffs/**").hasAnyAuthority(SUPER_ADMIN_CREATE.name(), ADMIN_CREATE.name())
                .requestMatchers(GET, "/staffs/**").hasAnyAuthority(SUPER_ADMIN_READ.name(), ADMIN_READ.name())
                .requestMatchers(PUT, "/staffs/**").hasAnyAuthority(SUPER_ADMIN_UPDATE.name(), ADMIN_UPDATE.name())
                .requestMatchers(DELETE, "/staffs/**").hasAnyAuthority(SUPER_ADMIN_DELETE.name(), ADMIN_DELETE.name())

                // Role Based Authorization [ Performances Endpoints ]
                .requestMatchers("/performances/**").hasAnyRole(
                    SUPER_ADMIN.name(),
                    ADMIN.name()
                )
                // Permission Based Authorization [ Performances Endpoints ]
                .requestMatchers(POST, "/performances/**").hasAnyAuthority(SUPER_ADMIN_CREATE.name(), ADMIN_CREATE.name())
                .requestMatchers(GET, "/performances/**").hasAnyAuthority(SUPER_ADMIN_READ.name(), ADMIN_READ.name())
                .requestMatchers(PUT, "/performances/**").hasAnyAuthority(SUPER_ADMIN_UPDATE.name(), ADMIN_UPDATE.name())
                .requestMatchers(DELETE, "/performances/**").hasAnyAuthority(SUPER_ADMIN_DELETE.name(), ADMIN_DELETE.name())

                // Role Based Authorization [ Gradings Endpoints ]
                .requestMatchers("/gradings/**").hasAnyRole(
                    SUPER_ADMIN.name(),
                    ADMIN.name()
                )
                // Permission Based Authorization [ Gradings Endpoints ]
                .requestMatchers(POST, "/gradings/**").hasAnyAuthority(SUPER_ADMIN_CREATE.name(), ADMIN_CREATE.name())
                .requestMatchers(GET, "/gradings/**").hasAnyAuthority(SUPER_ADMIN_READ.name(), ADMIN_READ.name())
                .requestMatchers(PUT, "/gradings/**").hasAnyAuthority(SUPER_ADMIN_UPDATE.name(), ADMIN_UPDATE.name())
                .requestMatchers(DELETE, "/gradings/**").hasAnyAuthority(SUPER_ADMIN_DELETE.name(), ADMIN_DELETE.name())

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
                logout.addLogoutHandler(logoutHandler)
                    .logoutUrl("/auth/logout")
                    .logoutSuccessHandler(
                        (request, response, authentication) -> SecurityContextHolder.clearContext()
                    )
        );

    return http.build();
  }
}
