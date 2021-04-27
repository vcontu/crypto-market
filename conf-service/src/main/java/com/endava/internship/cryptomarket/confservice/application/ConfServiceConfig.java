package com.endava.internship.cryptomarket.confservice.application;

import com.endava.internship.cryptomarket.confservice.data.UserRepository;
import com.endava.internship.cryptomarket.confservice.data.UsersInMemRepository;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.CLIENT;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.*;
import static java.time.LocalDateTime.now;

@Configuration
@Import({ValidationConfig.class, WebMvcConfiguration.class})
@EnableWebMvc
@ComponentScan(basePackages = {
        "com.endava.internship.cryptomarket.confservice.api",
        "com.endava.internship.cryptomarket.confservice.business",
        "com.endava.internship.cryptomarket.confservice.data"
})
public class ConfServiceConfig {

    @Bean
    @Profile("test")
    @Primary
    public UserRepository testRepository() {
        final UsersInMemRepository testRepository = new UsersInMemRepository();
        testRepository.save(User.builder().username("operat1").role(OPERAT)
                .email("operat1@gmail.com").status(ACTIVE)
                .createdOn(now()).build());
        testRepository.save(User.builder().username("operat2").role(OPERAT)
                .email("operat2@gmail.com").status(ACTIVE)
                .createdOn(now()).build());
        testRepository.save(User.builder().username("operat3").role(OPERAT)
                .email("operat3@gmail.com").status(SUSPND)
                .createdOn(now()).build());
        testRepository.save(User.builder().username("operat4").role(OPERAT)
                .email("operat4@gmail.com").status(INACTV)
                .createdOn(now()).build());
        testRepository.save(User.builder().username("client1").role(CLIENT)
                .email("client@gmail.com").status(ACTIVE)
                .createdOn(now()).build());
        testRepository.save(User.builder().username("client2").role(CLIENT)
                .email("client@gmail.com").status(ACTIVE)
                .createdOn(now()).build());
        return testRepository;
    }

}