package com.endava.upskill.confservice.api.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.endava.upskill.confservice.api.interceptor.RequesterUsernameInterceptor;
import com.endava.upskill.confservice.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class SpringWebConfig implements WebMvcConfigurer {

    private final UserService userService;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (var httpMessageConverter : converters) {
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter jackson) {
                DateTimeFormatter noNano = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                SimpleModule simpleModule = new SimpleModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(noNano));

                final ObjectMapper objectMapper = jackson.getObjectMapper();
                objectMapper.registerModule(simpleModule);
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final HandlerInterceptor interceptor = new RequesterUsernameInterceptor(userService);
        registry.addInterceptor(interceptor);
    }
}
