package com.blog.controller;

import com.blog.domain.dto.AddCommentDto;
import com.blog.domain.entity.Comment;
import com.blog.service.CommentService;
import com.blog.utils.BeanCopyPropertiesUtils;
import com.blog.utils.ResponseResult;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import static com.blog.constants.CommonConstants.ARTICLE_COMMENT;
import static com.blog.constants.CommonConstants.LINK_COMMENT;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    /**
     * 查询文章评论
     */
    @GetMapping("/commentList")
    public ResponseResult getCommentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.getCommentList(ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    /**
     * 添加评论
     */
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
        //将AddCommentDto类型转换为Comment类型
        Comment comment = BeanCopyPropertiesUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    /**
     * 查询友链评论
     * @return
     */
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize) {
        return commentService.getCommentList(LINK_COMMENT, null, pageNum, pageSize);
    }
}
