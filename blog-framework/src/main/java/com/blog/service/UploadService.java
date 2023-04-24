package com.blog.service;


import com.blog.utils.ResponseResult;
import org.springframework.web.multipart.MultipartFile;


public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
