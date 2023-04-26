package com.blog.service.impl;


import com.blog.enums.AppHttpCodeEnum;
import com.blog.exception.SystemException;
import com.blog.service.UploadService;
import com.blog.utils.FilePathUtils;
import com.blog.utils.ResponseResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class UploadServiceImpl implements UploadService {
    //...生成上传凭证，然后准备上传
    @Value("${oss.accessKey}")
    String accessKey;
    @Value("${oss.secretKey}")
    String secretKey;
    @Value("${oss.bucket}")
    String bucket;
    @Value(("${oss.cdnUrlPre}"))
    String cdnUrlPre;

    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        String originalFilename = img.getOriginalFilename();
        String filePathName = FilePathUtils.generateFilePath(originalFilename);
        String imageUrl = null;
        try {
            imageUrl = ossUpload(filePathName, img.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return ResponseResult.okResult(imageUrl);
    }

    //文件上传七牛云oss
    private String ossUpload(String key, InputStream inputStream) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名

        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                return cdnUrlPre + key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return null;
    }
}
