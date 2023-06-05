package com.blog.controller;

import com.blog.domain.dto.AdminLinkDto;
import com.blog.service.LinkService;
import com.blog.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/link")
public class LInkController {
    @Resource
    private LinkService linkService;

    /**
     * 分页查询所有友链-Admin
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("/list")
    public ResponseResult getLinkList(Integer pageNum, Integer pageSize, String name, String status) {
        return linkService.getAllLinkByAdmin(pageNum, pageSize, name, status);
    }

    /**
     * 添加友链
     * @param addLinkDto 添加链接dto
     * @return {@link ResponseResult}
     */
    @PostMapping
    public ResponseResult addLink(@RequestBody AdminLinkDto addLinkDto){
        return linkService.addLink(addLinkDto);
    }

    /**
     * 根据id查询友链
     * @param id id
     * @return {@link ResponseResult}
     */
    @GetMapping("/{id}")
    public ResponseResult getLinkOneById(@PathVariable Long id){
        return linkService.getLinkOneById(id);
    }

    /**
     * 修改友链
     * @param linkDto
     * @return {@link ResponseResult}
     */
    @PutMapping()
    public ResponseResult updateLink(@RequestBody AdminLinkDto linkDto){
        return linkService.updateLink(linkDto);
    }

    /**
     * 删除链接
     * @param id id
     * @return {@link ResponseResult}
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }
}
