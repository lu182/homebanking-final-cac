package com.cac.homebankingfinalcac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class HomebankingFinalCacApplication {

    public static void main(String[] args) {

        SpringApplication.run(HomebankingFinalCacApplication.class, args);
        System.out.println("Funcionando ok");
    }

}
