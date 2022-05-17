package com.myblog.service.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.myblog.service.web", "com.myblog.service.security", "com.myblog.service.base"})
@EnableDiscoveryClient
@ServletComponentScan
public class BlogWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogWebServiceApplication.class, args);
    }

}
