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
      "/",
      "/api/v1/auth/**",
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
    final String USERS_SECURED_ROUTE = "/api/v1/users/**";
    final String DEPARTMENT_SECURED_ROUTE = "/api/v1/departments/**";
    final String STAFF_SECURED_ROUTE = "/api/v1/staffs/**";
    final String PERFORMANCE_SECURED_ROUTE = "/api/v1/performances/**";
    final String GRADING_SECURED_ROUTE = "/api/v1/gradings/**";
    http
        .cors(withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            request -> request
                .requestMatchers(
                    WHITE_LIST
                ).permitAll()

                // Role Based Authorization [ Users Endpoints ]
                .requestMatchers(USERS_SECURED_ROUTE).hasAnyRole(
                    SUPER_ADMIN.name(),
                    ADMIN.name()
                )

                // Permission Based Authorization [ Users Endpoints ]
                .requestMatchers(POST, USERS_SECURED_ROUTE).hasAuthority(SUPER_ADMIN_CREATE.name())
                .requestMatchers(GET, USERS_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_READ.name(), ADMIN_READ.name())
                .requestMatchers(PUT, USERS_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_UPDATE.name())
                .requestMatchers(DELETE, USERS_SECURED_ROUTE).hasAuthority(SUPER_ADMIN_DELETE.name())

                // Role Based Authorization [ Departments Endpoints ]
                .requestMatchers(DEPARTMENT_SECURED_ROUTE).hasAnyRole(
                    SUPER_ADMIN.name(),
                    ADMIN.name()
                )
                // Permission Based Authorization [ Departments Endpoints ]
                .requestMatchers(POST, DEPARTMENT_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_CREATE.name(), ADMIN_CREATE.name())
                .requestMatchers(GET, DEPARTMENT_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_READ.name(), ADMIN_READ.name())
                .requestMatchers(PUT, DEPARTMENT_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_UPDATE.name(), ADMIN_UPDATE.name())
                .requestMatchers(DELETE, DEPARTMENT_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_DELETE.name(), ADMIN_DELETE.name())

                // Role Based Authorization [ Staffs Endpoints ]
                .requestMatchers(STAFF_SECURED_ROUTE).hasAnyRole(
                    SUPER_ADMIN.name(),
                    ADMIN.name()
                )
                // Permission Based Authorization [ Departments Endpoints ]
                .requestMatchers(POST, STAFF_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_CREATE.name(), ADMIN_CREATE.name())
                .requestMatchers(GET, STAFF_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_READ.name(), ADMIN_READ.name())
                .requestMatchers(PUT, STAFF_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_UPDATE.name(), ADMIN_UPDATE.name())
                .requestMatchers(DELETE, STAFF_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_DELETE.name(), ADMIN_DELETE.name())

                // Role Based Authorization [ Performances Endpoints ]
                .requestMatchers(PERFORMANCE_SECURED_ROUTE).hasAnyRole(
                    SUPER_ADMIN.name(),
                    ADMIN.name()
                )
                // Permission Based Authorization [ Performances Endpoints ]
                .requestMatchers(POST, PERFORMANCE_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_CREATE.name(), ADMIN_CREATE.name())
                .requestMatchers(GET, PERFORMANCE_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_READ.name(), ADMIN_READ.name())
                .requestMatchers(PUT, PERFORMANCE_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_UPDATE.name(), ADMIN_UPDATE.name())
                .requestMatchers(DELETE, PERFORMANCE_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_DELETE.name(), ADMIN_DELETE.name())

                // Role Based Authorization [ Gradings Endpoints ]
                .requestMatchers(GRADING_SECURED_ROUTE).hasAnyRole(
                    SUPER_ADMIN.name(),
                    ADMIN.name()
                )
                // Permission Based Authorization [ Gradings Endpoints ]
                .requestMatchers(POST, GRADING_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_CREATE.name(), ADMIN_CREATE.name())
                .requestMatchers(GET, GRADING_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_READ.name(), ADMIN_READ.name())
                .requestMatchers(PUT, GRADING_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_UPDATE.name(), ADMIN_UPDATE.name())
                .requestMatchers(DELETE, GRADING_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_DELETE.name(), ADMIN_DELETE.name())

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
                    .logoutUrl("/api/v1/auth/logout")
                    .logoutSuccessHandler(
                        (request, response, authentication) -> SecurityContextHolder.clearContext()
                    )
        );

    return http.build();
  }
}
