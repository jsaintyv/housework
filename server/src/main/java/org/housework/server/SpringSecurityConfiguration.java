package org.housework.server;

import javax.sql.DataSource;

import org.housework.server.models.RoleAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
    // Create 2 users for demo
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {    	
        auth.authenticationProvider(authProvider());        	        
    }
    
    @Autowired
    private UserDetailsService userDetailsService;
        
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);        
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

    // Secure the endpoins with HTTP Basic authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/user/list").hasAuthority("ADMIN")//.hasRole(RoleAuthority.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/api/tasks/**").hasAnyAuthority("ADMIN", "USER")//.hasRole(RoleAuthority.USER.name())
                .and()
                .csrf().disable()
                .formLogin().disable();
    } 
    
    
}
