package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.dto.TagListDto;
import com.blog.domain.entity.Tag;
import com.blog.domain.vo.PageVo;
import com.blog.domain.vo.TagVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.mapper.TagMapper;
import com.blog.service.TagService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @description 针对表【sg_tag(标签)】的数据库操作Service实现
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult tagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        Page<Tag> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        wrapper.like(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());
        page(page, wrapper);
        //封装数据返回
        List<TagVo> tagVo = BeanCopyPropertiesUtils.copyBeanList(page.getRecords(), TagVo.class);
        PageVo pageVo = new PageVo(tagVo, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(TagListDto tagListDto) {
        //1.首先根据标签名判断标签是否存在
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Tag::getName, tagListDto.getName());

        Tag getTag = getOne(queryWrapper);
        if (getTag != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TAG_IS_EXIST);
        }
        //2.将TagDto对象转换为Tag对象
        Tag tag = BeanCopyPropertiesUtils.copyBean(tagListDto, Tag.class);
        //3.添加标签
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        //根据标签id删除标签
        removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 点击修改按钮查询要修改的标签信息
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult selectTagInfo(Long id) {
        List<Tag> list = listByIds(Collections.singleton(id));
        if (list == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Tag tag = list.get(0);
        TagVo tagVo = new TagVo(tag.getId(), tag.getName(), tag.getRemark());
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(TagVo tagVo) {
        if (!StringUtils.hasText(tagVo.getName()) && !StringUtils.hasText(tagVo.getRemark())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Tag tag = BeanCopyPropertiesUtils.copyBean(tagVo, Tag.class);
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId, Tag::getName);
        List<Tag> list = list(wrapper);
        List<TagVo> tagVos = BeanCopyPropertiesUtils.copyBeanList(list, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }

}