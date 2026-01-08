package com.uniovi.sdi2425entrega121;

import com.uniovi.sdi2425entrega121.loggers.CustomLogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomLogoutHandler customLogoutHandler;

  public WebSecurityConfig(CustomLogoutHandler customLogoutHandler) {
    this.customLogoutHandler = customLogoutHandler;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SpringSecurityDialect securityDialect() {
    return new SpringSecurityDialect();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/css/**", "/images/**", "/script/**", "/", "/signup").permitAll()
        .antMatchers("/login").not().authenticated()
        .antMatchers("/changePassword").authenticated()
        .antMatchers("/employee/**").hasAnyAuthority("ROLE_ADMIN")
        .antMatchers("/log/**").hasAnyAuthority("ROLE_ADMIN")
        .antMatchers("/refuel/add").hasAnyAuthority("ROLE_STANDARD", "ROLE_ADMIN")
        .antMatchers("/refuel/list").hasAnyAuthority("ROLE_STANDARD", "ROLE_ADMIN")
        .antMatchers("/refuel/**").hasAnyAuthority("ROLE_STANDARD", "ROLE_ADMIN")
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login").successHandler(new CustomLoginSuccessHandler()).and()
        .logout()
        .logoutSuccessUrl("/login?logout=true")
        .logoutSuccessHandler(customLogoutHandler)
        .permitAll()
        .and()
        .exceptionHandling()
        .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/accessDenied"));
  }
}
