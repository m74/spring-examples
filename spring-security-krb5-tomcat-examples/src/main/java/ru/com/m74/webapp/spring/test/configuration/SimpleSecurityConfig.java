package ru.com.m74.webapp.spring.test.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author mixam
 * @since 15.05.16 19:44
 */
//@Configuration
//@EnableWebSecurity
public class SimpleSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable();
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated();

        http.formLogin()
                .loginProcessingUrl("/j_spring_security_check")
//                .usernameParameter("username")
//                .passwordParameter("password")

                .loginPage("/login/")
                .permitAll();

        http.logout()
                .logoutUrl("/j_spring_security_logout")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }

}
