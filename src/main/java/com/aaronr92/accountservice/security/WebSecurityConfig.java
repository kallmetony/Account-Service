package com.aaronr92.accountservice.security;

import com.aaronr92.accountservice.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    DaoAuthenticationProvider authenticationProvider;

    @Autowired
    AccessDeniedHandler accessDeniedHandler;

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint) // Handle auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/auth/singup").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/changepass").hasAnyAuthority(Role.ROLE_USER.name(),
                        Role.ROLE_ACCOUNTANT.name(),
                        Role.ROLE_ADMINISTRATOR.name())
                .antMatchers(HttpMethod.GET, "/api/empl/payment").hasAnyAuthority(Role.ROLE_USER.name(),
                        Role.ROLE_ACCOUNTANT.name())
                .antMatchers(HttpMethod.POST, "/api/acct/payments").hasAuthority(Role.ROLE_ACCOUNTANT.name())
                .antMatchers(HttpMethod.PUT, "/api/acct/payments").hasAuthority(Role.ROLE_ACCOUNTANT.name())
                .antMatchers("/api/admin/**").hasAuthority(Role.ROLE_ADMINISTRATOR.name())
                .antMatchers(HttpMethod.GET, "/api/security/**").hasAuthority(Role.ROLE_AUDITOR.name())
                .antMatchers("/actuator/**").hasAuthority(Role.ROLE_ADMINISTRATOR.name())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }
}
