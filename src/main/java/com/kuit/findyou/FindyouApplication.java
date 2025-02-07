package com.kuit.findyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling  // 이걸 설정해줘야함
public class FindyouApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindyouApplication.class, args);
    }

}
