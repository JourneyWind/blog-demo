package com.blog.config;

import com.blog.filter.JwtAuthenticationTokenFilter;
import com.blog.handler.security.MyAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final MyAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, MyAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.authorizeRequests()
                .antMatchers("/user/login").permitAll()
                .anyRequest().permitAll();
        security.exceptionHandling()
                        .authenticationEntryPoint(authenticationEntryPoint);
        security.csrf().disable();
        security.cors();
        security.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        security.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        security.logout().disable();
        return security.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
