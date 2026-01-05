package io.catalyte.training.health.config;

import io.catalyte.training.health.auth.AuthFilter;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {

  private static final String[] AUTH_WHITELIST = {
      // -- swagger ui
      "/swagger-resources/**",
      "/swagger-ui.html",
      "/v2/api-docs",
      "/webjars/**"
  };

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            //.allowedOrigins("http://localhost:5173");
          .allowedOrigins("*");
      }
    };
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .addFilterBefore(new AuthFilter(), AuthFilter.class)
        .authorizeHttpRequests((authz) -> authz
            .requestMatchers(AUTH_WHITELIST).permitAll()
            .requestMatchers(HttpMethod.GET).permitAll()
            // Require authentication for all other requests
            .anyRequest().authenticated()
        )
        .sessionManagement(mgmt -> mgmt.disable())
        .csrf((csrf) -> csrf.disable());

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) ->  web.ignoring()
        .requestMatchers(HttpMethod.OPTIONS)
        .requestMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
            "/configuration/security", "/swagger-ui.html", "/webjars/**", "/login");
  }


/*  *//**
   * Disables the login page for Spring boot and cross referencing
   *//*
  @Override
  protected void configure(HttpSecurity security) throws Exception {
    security.httpBasic().disable();
    security.csrf().disable()
        .authorizeRequests().antMatchers(HttpMethod.PUT, "/users").authenticated().and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }*/

}
