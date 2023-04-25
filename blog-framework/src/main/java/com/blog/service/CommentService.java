package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.entity.Comment;
import com.blog.utils.ResponseResult;
import org.springframework.stereotype.Service;

/**
 * @description 针对表【sg_comment(评论表)】的数据库操作Service
 */

public interface CommentService extends IService<Comment> {

    ResponseResult getCommentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
