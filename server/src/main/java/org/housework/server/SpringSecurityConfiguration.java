package org.housework.server;

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
                .requestCache()                	
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/user/list").hasAuthority("ADMIN")//.hasRole(RoleAuthority.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/api/user/current").authenticated() //.hasRole(RoleAuthority.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/api/house/**").authenticated() 
                .antMatchers(HttpMethod.POST, "/api/tasks/**").authenticated() //.hasRole(RoleAuthority.USER.name())
                .antMatchers(HttpMethod.POST, "/login").permitAll() //.hasRole(RoleAuthority.USER.name())
                .antMatchers(HttpMethod.POST, "/register").permitAll() 
                .antMatchers(HttpMethod.POST, "/rejected").permitAll() 
                .antMatchers(HttpMethod.GET, "/index.html").permitAll() 
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/index.html")
                .loginProcessingUrl("/login")
                .usernameParameter("login")
                .passwordParameter("password")
                .defaultSuccessUrl("/api/user/current", true)
                .failureUrl("/rejected");
                
                /*
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler());
                */
    } 
    
    
}
