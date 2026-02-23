package com.tyut;

import com.tyut.config.WebMvcConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.tyut")
@MapperScan("com.tyut.mapper")
@EnableSwagger2
@EnableScheduling
public class Application{
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}