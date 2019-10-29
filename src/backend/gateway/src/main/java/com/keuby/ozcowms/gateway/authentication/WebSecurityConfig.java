package com.keuby.ozcowms.gateway.authentication;

import com.keuby.ozcowms.gateway.filter.UserAuthenticationLifecycleFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthenticationLifecycleFilter userAuthenticationLifecycleFilter;

    public WebSecurityConfig(UserAuthenticationLifecycleFilter userAuthenticationLifecycleFilter) {
        this.userAuthenticationLifecycleFilter = userAuthenticationLifecycleFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**").csrf().disable()
                .addFilterBefore(userAuthenticationLifecycleFilter, AbstractPreAuthenticatedProcessingFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                    .antMatchers("/api/v1/miniapp/user/**").permitAll()
                    .anyRequest().authenticated();
    }
}
