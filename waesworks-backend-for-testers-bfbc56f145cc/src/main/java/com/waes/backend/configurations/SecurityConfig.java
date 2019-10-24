package com.waes.backend.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.util.Properties;
import javax.sql.DataSource;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String ADMIN = "ADMIN";
  private static final String USER = "USER";

  @Autowired
  private AuthenticationEntryPoint basicAuthEntryPoint;

  @Autowired
  private DataSource dataSource;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers(GET, "/**/users/all").hasRole(ADMIN)
        .antMatchers(GET, "/**/users/access").authenticated()
        .antMatchers(PUT, "/**/users").authenticated()
        .antMatchers(DELETE, "/**/users").authenticated()
        .anyRequest().anonymous()
        .and()
        .httpBasic().authenticationEntryPoint(basicAuthEntryPoint)
        .and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(STATELESS)
        .and()
        .logout().disable()
        .formLogin().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(inMemoryUserDetailsManager());
  }

  @Bean
  public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    final Properties users = new Properties();
    users.put("dev", encoder().encode("wizard") + ",ROLE_" + USER);
    users.put("tester", encoder().encode("maniac") + ",ROLE_" + USER);
    users.put("admin", encoder().encode("hero") + ",ROLE_" + ADMIN);
    return new InMemoryUserDetailsManager(users);
  }

  @Override
  public void configure(WebSecurity web) {
    web
        .ignoring()
        .antMatchers(GET, "/swagger-ui.html")
        .antMatchers(OPTIONS, "/**");
  }

  @Bean
  PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
}