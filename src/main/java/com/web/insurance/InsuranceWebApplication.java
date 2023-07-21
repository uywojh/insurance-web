package com.web.insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.web.insurance.mapper")
public class InsuranceWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsuranceWebApplication.class, args);
    }

}
