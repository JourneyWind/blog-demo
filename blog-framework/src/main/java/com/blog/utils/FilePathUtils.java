package com.blog.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class FilePathUtils {
    public static String generateFilePath(String fileName){
        //根据日期生成路径
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String time = dateTimeFormatter.format(LocalDateTime.now());
        //uuid为文件名
        String uuidName = UUID.randomUUID().toString().replaceAll("-", "");
        //后缀保持一致
        int indexOf = fileName.lastIndexOf(".");
        String substring = fileName.substring(indexOf);
        return new StringBuilder().append(time).append(uuidName).append(substring).toString();
    }
}
