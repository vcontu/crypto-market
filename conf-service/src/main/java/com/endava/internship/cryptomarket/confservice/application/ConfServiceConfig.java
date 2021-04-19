package com.endava.internship.cryptomarket.confservice.application;

import com.endava.internship.cryptomarket.confservice.data.UserRepository;
import com.endava.internship.cryptomarket.confservice.data.UsersInMemRepository;
import com.endava.internship.cryptomarket.confservice.data.model.Roles;
import com.endava.internship.cryptomarket.confservice.data.model.Status;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;

import static java.time.LocalDateTime.now;

@Configuration
@ComponentScan(basePackages = {
        "com.endava.internship.cryptomarket.confservice.api",
        "com.endava.internship.cryptomarket.confservice.business",
        "com.endava.internship.cryptomarket.confservice.data"
})
public class ConfServiceConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @Bean
    @Profile("test")
    @Primary
    public UserRepository testRepository() {
        final UsersInMemRepository testRepository = new UsersInMemRepository();
        testRepository.save(User.builder().username("operat1").role(Roles.OPERAT)
                .email("operat1@gmail.com").status(Status.ACTIVE)
                .createdOn(now()).build());
        testRepository.save(User.builder().username("operat2").role(Roles.OPERAT)
                .email("operat2@gmail.com").status(Status.ACTIVE)
                .createdOn(now()).build());
        testRepository.save(User.builder().username("operat3").role(Roles.OPERAT)
                .email("operat3@gmail.com").status(Status.SUSPND)
                .createdOn(now()).build());
        testRepository.save(User.builder().username("operat4").role(Roles.OPERAT)
                .email("operat4@gmail.com").status(Status.INACTV)
                .createdOn(now()).build());
        testRepository.save(User.builder().username("client1").role(Roles.CLIENT)
                .email("client@gmail.com").status(Status.ACTIVE)
                .createdOn(now()).build());
        testRepository.save(User.builder().username("client2").role(Roles.CLIENT)
                .email("client@gmail.com").status(Status.ACTIVE)
                .createdOn(now()).build());
        return testRepository;
    }

}