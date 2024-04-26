package com.example.paysonix.configuration;

import com.example.paysonix.network.SimpleFormMapHttpMessageConverter;
import com.example.paysonix.network.AuthTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthTokenInterceptor authTokenInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authTokenInterceptor).addPathPatterns("/**");
    }

    @Bean
    public HttpMessageConverter<Map<String, String>> simpleFormMapHttpMessageConverter() {
        return new SimpleFormMapHttpMessageConverter();
    }
}
