/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author Ebenezer
 */
//@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    
    @Bean
    RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor();
    }

    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor(getRequestInterceptor());

    }
}
