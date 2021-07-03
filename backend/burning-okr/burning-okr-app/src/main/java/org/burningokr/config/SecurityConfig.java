package org.burningokr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  public static final String API_PREFIX_CONFIG_NAME = "${system.configuration.api-endpoint:/api}";

  @Value(API_PREFIX_CONFIG_NAME)
  private String apiPrefix;

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(patterns());
  }

  private String[] patterns() {
    return new String[] {
      "/v2/api-docs",
      "/swagger-resources/configuration/ui",
      "/swagger-resources/configuration/security",
      "/csrf",
      "/swagger-resources",
      "/swagger-ui.html**",
      "/webjars/**",
      apiPrefix + "/init/**",
      apiPrefix + "/local-users/password",
      apiPrefix + "/local-users/forgot-password",
      apiPrefix + "/oAuthFrontendDetails",
      "/wsregistry"
    };
  }
}
