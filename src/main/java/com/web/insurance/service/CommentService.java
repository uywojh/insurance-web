package com.web.insurance.service;

import com.web.insurance.po.Comment;

import java.util.List;

/**
 * Created on 2020/4/10
 * Package com.web.insurance.service
 *
 * @author dsy
 */
public interface CommentService {

    List<Comment> findCommentsByBlogId(Integer blogId);

    void saveComment(Comment comment);
}
