package com.waes.backend.configurations;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ABasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

  @Override
  public void commence(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AuthenticationException authException
  ) throws IOException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    setRealmName("WAES");
    super.afterPropertiesSet();
  }
}