package com.ignite.aaronpractice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.ignite.aaronpractice.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // TODO:
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails jamesBondUser = User.builder()
                .username("James Bond")
                .password(passwordEncoder.encode("password1"))
                .roles(STUDENT.name()) // ROLE_STUDENT
                .build();


        UserDetails lindeUser = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("password1"))
                .roles(ADMIN.name())  // ROLE_ADMIN
                .build();

   UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password1"))
                .roles(ADMINTRAINEE.name()) //ROLE_ADMINTRAINEE
                .build();


        return new InMemoryUserDetailsManager(jamesBondUser, lindeUser , tomUser);
    }
}