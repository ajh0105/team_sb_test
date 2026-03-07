package com.example.tour.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfig {

    @Value("${file.upload-dir}")
    public String uploadDir;    //나중에 호출할 때 이 변수로 가져올 수 있음
}
