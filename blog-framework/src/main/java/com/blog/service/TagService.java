package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.dto.TagListDto;
import com.blog.domain.entity.Tag;
import com.blog.domain.vo.TagVo;
import com.blog.utils.ResponseResult;


/**
* @description 针对表【sg_tag(标签)】的数据库操作Service
*/
public interface TagService extends IService<Tag> {
    ResponseResult tagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(TagListDto tagListDto);

    ResponseResult deleteTag(Long id);

    ResponseResult selectTagInfo(Long id);

    ResponseResult updateTag(TagVo tagVo);
}
