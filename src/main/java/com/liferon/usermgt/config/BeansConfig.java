/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.config;

import java.sql.Date;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Ebenezer
 */
@Configuration
public class BeansConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
                
        modelMapper.addConverter(new Converter<String, Date>(){
                @Override
                public Date convert(MappingContext<String, Date> context) {
                    return context.getSource() == null || context.getSource().isEmpty() ? null : Date.valueOf(context.getSource());
                }                
            });
        
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
    
    @Bean 
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    /*
    @Bean
    public MapperFacade mapper() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        
        return mapperFactory.getMapperFacade();
    }
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}   
