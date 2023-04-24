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
    private final JwtAuthenticationTokenFilter filter;
    private final MyAuthenticationEntryPoint authenticationEntryPoint;
//    private final MyAccessDeniedHandler accessDeniedHandler;
    @Autowired
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtAuthenticationTokenFilter filter, MyAuthenticationEntryPoint authenticationEntryPoint/*, MyAccessDeniedHandler accessDeniedHandler*/) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.filter = filter;
        this.authenticationEntryPoint = authenticationEntryPoint;
//        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.authorizeRequests()
                //对于登录接口 允许匿名访问
                .antMatchers("/login").anonymous()
                .antMatchers("/link/getAllLink","/logout").authenticated()
                //对于其他请求全部允许访问
                .anyRequest().permitAll();
        security.csrf().disable();
        security.cors();
        security.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//不创建会话
        security.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        security.logout().disable();
        security.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                /*.accessDeniedHandler(accessDeniedHandler)*/;
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
