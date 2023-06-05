package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.dto.AdminLinkDto;
import com.blog.domain.entity.Link;
import com.blog.utils.ResponseResult;


/**
* @description 针对表【sg_link(友链)】的数据库操作Service
*/

public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getAllLinkByAdmin(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(AdminLinkDto addLinkDto);

    ResponseResult getLinkOneById(Long id);

    ResponseResult updateLink(AdminLinkDto linkDto);

    ResponseResult deleteLink(Long id);
}
