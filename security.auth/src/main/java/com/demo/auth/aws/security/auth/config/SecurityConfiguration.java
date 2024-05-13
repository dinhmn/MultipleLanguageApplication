package com.demo.auth.aws.security.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Value("${role.user}")
  private String user;

  @Value("${role.admin}")
  private String admin;

  private final CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

  public SecurityConfiguration(
      CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler) {

    this.customizeAuthenticationSuccessHandler = customizeAuthenticationSuccessHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(
            request ->
                request
                    .requestMatchers("/")
                    .permitAll()
                    .requestMatchers("/admin/*")
                    .hasRole(admin)
                    .requestMatchers("/user/*")
                    .hasAnyRole(admin, user)
                    .anyRequest()
                    .authenticated())
        .oauth2Login(
            oauth ->
                oauth
                    .redirectionEndpoint(endPoint -> endPoint.baseUri("/login/oauth2/code/cognito"))
                    .userInfoEndpoint(
                        userInfoEndpointConfig ->
                            userInfoEndpointConfig.userAuthoritiesMapper(userAuthoritiesMapper()))
                    .successHandler(customizeAuthenticationSuccessHandler))
        .logout(logout -> logout.logoutSuccessUrl("/"));
    return http.build();
  }

  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

      try {
        OidcUserAuthority oidcUserAuthority =
            (OidcUserAuthority) new ArrayList<>(authorities).get(0);

        mappedAuthorities =
            ((ArrayList<?>) oidcUserAuthority.getAttributes().get("cognito:groups"))
                .stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toSet());
      } catch (Exception exception) {
        System.out.println("Not Authorized!");

        System.out.println(exception.getMessage());
      }

      return mappedAuthorities;
    };
  }
}
