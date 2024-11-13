package com.example.springboot3_backend_jwt_authentication_cart.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.springboot3_backend_jwt_authentication_cart.security.jwt.AuthEntryPointJwt;
import com.example.springboot3_backend_jwt_authentication_cart.security.jwt.AuthTokenFilter;
import com.example.springboot3_backend_jwt_authentication_cart.security.services.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }
  
//@Override
//public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//  authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//}

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }
  
//@Bean
//@Override
//public AuthenticationManager authenticationManagerBean() throws Exception {
//  return super.authenticationManagerBean();
//}
  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
//@Override
//protected void configure(HttpSecurity http) throws Exception {
//  http.cors().and().csrf().disable()
//    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//    .authorizeRequests().antMatchers("/api/auth/**").permitAll()
//    .antMatchers("/api/test/**").permitAll()
//    .anyRequest().authenticated();
//
//  http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//}
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
    	//1. [Enable CORS and] disable CSRF
    	.csrf(csrf -> csrf.disable())
    	//2. Set  unauthorized requests exception handler
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        //3. Set session management to stateless
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        //4. Set permissions on endpoints
        .authorizeHttpRequests(auth -> auth
        		// Our public endpoints
        	  .requestMatchers("/api/auth/**").permitAll()
              .requestMatchers("/api/test/**").permitAll()
              // Our private endpoints
              .anyRequest().authenticated()
        );
    
    http.authenticationProvider(authenticationProvider());
    
    //5. Add JWT token filter
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
}