package com.myblog.service.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"com.myblog.service"})
@EnableDiscoveryClient
@EnableFeignClients
@ServletComponentScan
@EnableScheduling
public class BlogAdminServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogAdminServiceApplication.class, args);
    }
}
