package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.dto.AdminLinkDto;
import com.blog.domain.entity.Link;
import com.blog.domain.vo.LinkVo;
import com.blog.domain.vo.PageVo;
import com.blog.enums.AppHttpCodeEnum;
import com.blog.mapper.LinkMapper;
import com.blog.service.LinkService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.blog.constants.CommonConstants.LINK_STATUS_NORMAL;


/**
 * @description 针对表【sg_link(友链)】的数据库操作Service实现
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Resource
    private LinkMapper linkMapper;

    /**
     * 查询所有友链
     */
    public ResponseResult getAllLink() {
        //1.查询所有审核通过的友链
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, LINK_STATUS_NORMAL);
        List<Link> lists = list(queryWrapper);
        //2.转换为Vo对象
        List<LinkVo> linkVos = BeanCopyPropertiesUtils.copyBeanList(lists, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    /**
     * 分页查询所有友链-Admin
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getAllLinkByAdmin(Integer pageNum, Integer pageSize, String name, String status) {
        //1.根据友链名(模糊查询)和状态进行查询
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status), Link::getStatus, status);
        queryWrapper.like(StringUtils.hasText(name), Link::getName, name);

        //2.分页查询
        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);

        //3.将当前页中的Link对象转换为LinkVo对象
        List<Link> links = page.getRecords();
        List<LinkVo> linkVos = BeanCopyPropertiesUtils.copyBeanList(links, LinkVo.class);
        //4.将LinkVo对象转换为LinkAdminVo对象
        PageVo pageVo = new PageVo(linkVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 添加友链
     * @param addLinkDto 添加链接dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult addLink(AdminLinkDto addLinkDto) {
        //1.首先根据友链名称查询要添加的友链是否存在
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Link::getName, addLinkDto.getName());
        Link link = getOne(queryWrapper);
        if (!Objects.isNull(link)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LINK_IS_EXIST);
        }
        //2.添加友链
        //2.1将AddLinkDto对象转为Link对象
        Link addLink = BeanCopyPropertiesUtils.copyBean(addLinkDto, Link.class);
        save(addLink);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkOneById(Long id) {
        //1.根据id查询友链
        Link link = getById(id);
        //2.将Link对象封装为LinkVo对象
        LinkVo linkVo = BeanCopyPropertiesUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult updateLink(AdminLinkDto linkDto) {
        //1.判断LinkDto对象值是否为空
        if (!StringUtils.hasText(linkDto.getName()) ||
                !StringUtils.hasText(linkDto.getAddress()) ||
                !StringUtils.hasText(String.valueOf(linkDto.getStatus())) ||
                !StringUtils.hasText(linkDto.getLogo()) ||
                !StringUtils.hasText(linkDto.getDescription())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CONTENT_IS_BLANK);
        }
        //2.将LinkVo对象转换为Link对象
        Link link = BeanCopyPropertiesUtils.copyBean(linkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long id) {
                boolean result = removeById(id);
        if (result == false) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_LINK_FAIL);
        }
        return ResponseResult.okResult();
    }


//    /**
//     * 更新友链状态
//     *
//     * @param linkStatusDto 链接状态dto
//     * @return {@link ResponseResult}
//     */
//    @Override
//    public ResponseResult updateLinkStatus(LinkStatusDto linkStatusDto) {
////        1.根据友链id设置友链status
//        UpdateWrapper<Link> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq(LINK_ID, linkStatusDto.getId());
//        updateWrapper.set(LINK_STATUS, linkStatusDto.getStatus());
//        linkMapper.update(null, updateWrapper);
//        return ResponseResult.okResult();
//    }
}
