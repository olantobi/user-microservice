/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.security;

import com.liferon.usermgt.component.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Configuration
@EnableWebSecurity
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);//.userDetailsService(userDetailsService);
    }
     
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {        
        return super.authenticationManagerBean();
    }
    /*
    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {// @formatter:off
        auth.inMemoryAuthentication().withUser("sunnyben").password("123456").roles("ADMIN").and().withUser("olantobi")
                .password("adetoberu").roles("SYSADMIN");
    }
    
     
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("olantobi").password("adetoberu").roles("SYSADMIN");
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        //auth.inMemoryAuthentication().withUser("john").password("123").roles("USER").and().withUser("tom")
                //.password("111").roles("ADMIN");
    }
*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated();
               // .and()                
                //.formLogin().loginPage("/login").successHandler(new CustomLoginSuccessHandler()).permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/", 
                        "/swagger-ui.html", 
                        "/webjars/**",                         
	    		"/swagger-resources/**", 
	    		"/v2/api-docs", 
	    		"/encrypt-password",
                        "/liquibase",
                        "/login", 
                        "/health",
                        "/metrics",
                        "/trace",
	    		"/info,");
    }   
}
