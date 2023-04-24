package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.entity.Comment;
import com.blog.domain.vo.CommentVo;
import com.blog.domain.vo.PageVo;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import com.blog.service.UserService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.ResponseResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.blog.constants.CommonConstants.ROOT_ID;


/**
 * @description 针对表【sg_comment(评论表)】的数据库操作Service实现
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    private UserService userService;

    @Override
    public ResponseResult getCommentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        //查询对应文章根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,articleId).eq(Comment::getRootId,ROOT_ID);
        List<Comment> list = list(queryWrapper);
        List<CommentVo> commentVos = toCommentListVo(list);
        PageInfo<CommentVo> commentVoPageInfo = new PageInfo<>(commentVos);
        PageVo pageVo = new PageVo(commentVoPageInfo.getList(), commentVoPageInfo.getTotal());
        return ResponseResult.okResult(pageVo);
    }


//
//    /**
//     * 查询评论
//     * @param commentType
//     * @param articleId
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    @Override
//    public ResponseResult getCommentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
//        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
////        1.判断参数中是否含有articleId，含有表示是文章评论，没有是友链评论，有articleId参数就添加条件
//        queryWrapper.eq(ARTICLE_COMMENT.equals(commentType),Comment::getArticleId, articleId);
////          1.1：-1表示当前评论为根评论
//        queryWrapper.eq(Comment::getRootId, ROOT_ID);
////          1.2设置评论类型
//        queryWrapper.eq(Comment::getType,commentType);
//
////        2.分页查询
//        Page<Comment> page = new Page(pageNum, pageSize);
//        page(page, queryWrapper);
//
////        3.将Comment对象封装为CommentVo对象
//        List<CommentVo> commentVoLists = toCommentListVo(page.getRecords());
//
////        4.获取到与根评论相关的子评论
//        commentVoLists.stream().map(
//                commentVo -> {
////                    4.1查询对应的子评论
//                    List<CommentVo> children = getChildren(commentVo.getId());
//                    commentVo.setChildren(children);
//                    return commentVo;
//                }
//        ).collect(Collectors.toList());
//
//        return ResponseResult.okResult(new PageVo(commentVoLists, page.getTotal()));
//
//    }
//
//    /**
//     * 添加评论
//     * @param comment
//     * @return
//     */
//    public ResponseResult addComment(Comment comment) {
////        1.字段填充功能已经在MyMetaObjectHandler实现
////        2.判断评论内容是否为空
//        if (!StringUtils.hasText(comment.getContent())) {
//            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
//        }
//        save(comment);
//        return null;
//    }
//
//
    /**
     * 将Comment对象封装为CommentVo对象，
     * 并根据createBy（创建评论人id）查询用户昵称赋值给Nickname
     * 查询子评论的用户昵称赋值给Nickname
     * @param list
     * @return
     */
    private List<CommentVo> toCommentListVo(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyPropertiesUtils.copyBeanList(list, CommentVo.class);
        //遍历Vo集合
        commentVos.stream().map(commentVo -> {
            //通过createBy（创建评论人id）查询用户昵称
            String nickname = userService.getById(commentVo.getCreateBy()).getNickName();
            //将查询到的创建评论的用户昵称复制给commentVo对象
            commentVo.setUsername(nickname);
            //如果toCommentUserId不为-1表示为子评论
            //通过toCommentUserId查询回复评论的用户的昵称并赋值
            if (commentVo.getToCommentUserId() != -1) {
                String commentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(commentUserName);
            }
            return commentVo;
        }).collect(Collectors.toList());
        return commentVos;
    }
//
//    /**
//     * 根据根评论rootId查询对应的子评论的集合
//     *
//     * @param id
//     * @return
//     */
//    private List<CommentVo> getChildren(Long id) {
//        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Comment::getRootId, id);
////        按照创建时间进行降序排序
//        queryWrapper.orderByAsc(Comment::getCreateTime);
//        List<Comment> lists = list(queryWrapper);
//        List<CommentVo> commentVos = toCommentListVo(lists);
//        return commentVos;
    }