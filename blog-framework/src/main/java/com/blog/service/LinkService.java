package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.entity.Link;
import com.blog.utils.ResponseResult;


/**
* @description 针对表【sg_link(友链)】的数据库操作Service
*/

public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
//
//    ResponseResult getAllLinkByAdmin(Integer pageNum, Integer pageSize, LinkDto linkDto);
//
//    ResponseResult addLink(AddLinkDto addLinkDto);
//
//    ResponseResult deleteLink(Long id);
//
//    ResponseResult getLinkOneById(Long id);
//
//    ResponseResult updateLink(LinkDto linkDto);
//
//    ResponseResult updateLinkStatus(LinkStatusDto linkStatusDto);
}
