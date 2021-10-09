package com.ignite.aaronpractice.security;

import com.ignite.aaronpractice.auth.ApplicationUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.ignite.aaronpractice.security.ApplicationUserRole.STUDENT;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final ApplicationUsersService applicationUsersService;


    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUsersService applicationUsersService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUsersService = applicationUsersService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and() // for production
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .anyRequest()
                .authenticated()
                .and()
//                .httpBasic();
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/courses", true)
                    .passwordParameter("password") // ref the form name prop in the form inputs
                    .usernameParameter("username")
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("somethingVerySecure")// default 2 weeks
                    .rememberMeParameter("remember-me")


                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // disable when using csrf
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID","remember-me")
                .logoutSuccessUrl("/login");

    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails jamesBondUser = User.builder()
//                .username("James Bond")
//                .password(passwordEncoder.encode("password1"))
//                .authorities(STUDENT.getGrantedAuthorities())
////                .roles(STUDENT.name()) // ROLE_STUDENT
//                .build();
//
//
//        UserDetails lindeUser = User.builder()
//                .username("Linda")
//                .password(passwordEncoder.encode("password1"))
//                .authorities(ADMIN.getGrantedAuthorities())
////                .roles(ADMIN.name())  // ROLE_ADMIN
//                .build();
//
//        UserDetails tomUser = User.builder()
//                .username("tom")
//                .password(passwordEncoder.encode("password1"))
//                .authorities(ADMINTRAINEE.getGrantedAuthorities())
////                .roles(ADMINTRAINEE.name()) //ROLE_ADMINTRAINEE
//                .build();
//



//        return new InMemoryUserDetailsManager(jamesBondUser, lindeUser, tomUser);
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider  daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUsersService);
        return provider;

    }

}