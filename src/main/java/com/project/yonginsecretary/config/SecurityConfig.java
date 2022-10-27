package com.project.yonginsecretary.config;

import com.project.yonginsecretary.auth.CustomAccessDeniedHandler;
import com.project.yonginsecretary.auth.CustomAuthenticationEntryPoint;
import com.project.yonginsecretary.auth.LoginFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URLEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/content/1/write").access("hasAuthority('ADMIN')")
                .antMatchers("/", "/css/**", "/user/login", "/user/signup", "/message", "/error")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("loginId")
                .loginPage("/user/login")
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/message?url=/&msg="+ URLEncoder.encode("로그인에 성공했습니다!", "UTF-8"))
                .failureHandler(new LoginFailureHandler());
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler());
    }
}