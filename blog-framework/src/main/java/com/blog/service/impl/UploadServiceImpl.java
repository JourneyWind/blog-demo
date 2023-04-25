package com.blog.service.impl;


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
        }
        return ResponseResult.okResult(imageUrl);
    }

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
//
//    /**
//     * 文件上传阿里云OSS
//     *
//     * @param imgFile
//     * @return
//     */
//    @Override
//    public ResponseResult uploadImg(MultipartFile imgFile) {
////        1.获取到原始文件名
//        String originalFilename = imgFile.getOriginalFilename();
//        String fileName = ImgUtils.generateFilePath(originalFilename);
//        String imgUrl = ossUpload(imgFile, fileName);
//        return ResponseResult.okResult(imgUrl);
//    }
//
//
//    private String ossUpload(MultipartFile multipartFile, String fileName) {
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder()
//                .build(OssUtils.END_POINT, OssUtils.ACCESS_KEY_ID, OssUtils.ACCESS_KEY_SECRET);
//
//
//        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
//        String objectName = "sangengblog/" + fileName;
//        try {
//
//            InputStream inputStream = multipartFile.getInputStream();
//            // 创建PutObject请求。
//            ossClient.putObject(OssUtils.BUCKET_NAME, objectName, inputStream);
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, "
//                    + "but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
////        返回对象存储中的图片路径
//        return "https://" + OssUtils.BUCKET_NAME + "." + OssUtils.END_POINT + "/" + objectName;
//    }
}
