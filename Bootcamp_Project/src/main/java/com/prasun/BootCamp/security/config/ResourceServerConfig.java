package com.prasun.BootCamp.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "bootxcamp";



    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

 //       http.authorizeRequests()
//                .mvcMatchers(HttpMethod.GET,"/","/user/**","/current/user")
//                .hasAnyRole("USER","ADMIN","CUSTOMER")
//                .mvcMatchers(HttpMethod.POST,"/login","/registerUser","/register/customer","/register/seller")
//                .hasAnyRole("USER","ADMIN")
//                .mvcMatchers(HttpMethod.GET,"/customer/profile")
//                .hasAnyRole("CUSTOMER")
//                .anyRequest()
//                .denyAll()
//                .and()
//                .csrf()
//                .disable();

        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET,"/","/user/**","/current/user")
                .hasAnyRole("ADMIN","CUSTOMER","SELLER")
                .mvcMatchers(HttpMethod.POST,"/registerUser","/customer/register","/register/seller","/customer/address")
                .hasAnyRole("ADMIN","CUSTOMER")
                .mvcMatchers("/customer/**")
                .hasAnyRole("CUSTOMER")
                .mvcMatchers("/seller/**")
                .hasAnyRole("SELLER","ADMIN")
                .mvcMatchers("/admin/**")
                .hasAnyRole("ADMIN")
                .anyRequest()
                .denyAll()
                .and()
                .csrf()
                .disable();

    }



}