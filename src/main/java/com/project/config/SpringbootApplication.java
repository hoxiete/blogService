package com.project.config;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@Component
@ComponentScan({"com.project.controller","com.project.service","com.project.config"})
@MapperScan("com.project.mapper")
public class SpringbootApplication {
   public static Logger logger = LoggerFactory.getLogger(SpringbootApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
       logger.info("============= SpringBoot Start Success =============");
    }


}
