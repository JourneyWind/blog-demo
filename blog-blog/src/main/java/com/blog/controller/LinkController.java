package com.blog.controller;


import com.blog.service.LinkService;
import com.blog.utils.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Resource
    private LinkService linkService;

    /**
     * 查询所有友链
     */
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }

}
