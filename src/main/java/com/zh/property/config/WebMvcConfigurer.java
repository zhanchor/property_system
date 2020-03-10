package com.zh.property.config;

import com.zh.property.interceptor.AdminInterceptor;
import com.zh.property.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Bean
    public LoginInterceptor getLoginIntercepter(){
        return new LoginInterceptor();
    }

    @Bean
    public AdminInterceptor getAdminInterceptor(){
        return new AdminInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        String[] exclude = new String[] {"/js/**","/img/**","/css/**","/webapp/**","/forelogin","/foreregister"};
        registry.addInterceptor(getLoginIntercepter()).addPathPatterns("/**").excludePathPatterns(exclude);
        registry.addInterceptor(getAdminInterceptor()).addPathPatterns("/**").excludePathPatterns(exclude);
    }



}
