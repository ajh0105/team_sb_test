package com.example.tour.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

public class FileUploadUtil {

    public static String saveFile(String uploadDir, MultipartFile file) throws Exception{
        if (file.isEmpty()) return null;

        String original = file.getOriginalFilename();   //예를 들어 C:\ajh\images\abc.jpg 를 original이 기억하도록 함
        String ext = original.substring(original.lastIndexOf(".")); // origin에서 마지막에 위치한 ,을 찾아라
        //임의의 파일명으로 생성 => UUID -> 2c38-4c67-6789.jpg
        String savedName = UUID.randomUUID() + ext;    //같은걸 넣어도 중복되지 않도록 함

        File dest = new File(uploadDir + savedName);     //C:\\upcload\\2c38-4c67-6789.jpg
        file.transferTo(dest);      //실제 업로드

        return savedName;
    }
}
