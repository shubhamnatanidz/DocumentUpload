package com.example.uploadDocument.config;

import com.example.uploadDocument.constants.Constants;
import com.example.uploadDocument.model.User;
import com.example.uploadDocument.utils.UserHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // Function to encode the password
    // assign to the particular roles.
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        List<User> users = UserHelper.getUsers();
        for (User user : users) {
            auth
                    .inMemoryAuthentication()
                    .withUser(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .and();
        }
    }

    // Configuring the api
    // according to the roles.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/files/upload")
                .hasRole(Constants.ADMIN_ROLE)
                .antMatchers("/files/list")
                .hasAnyRole(Constants.ADMIN_ROLE, Constants.USER_ROLE)
                .antMatchers("/files/delete/{id}")
                .hasAnyRole(Constants.ADMIN_ROLE)
                .antMatchers("/files/{fileId}")
                .hasAnyRole(Constants.ADMIN_ROLE, Constants.USER_ROLE)
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

}
