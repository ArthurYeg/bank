package com.example.test.bank.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyCustomFilter> loggingFilter() {
        FilterRegistrationBean<MyCustomFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new MyCustomFilter());
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }

    public class MyCustomFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {

            System.out.println("Request intercepted by MyCustomFilter");


            System.out.println("Response intercepted by MyCustomFilter");
        }

        @Override
        public void destroy() {
        }
    }
}
