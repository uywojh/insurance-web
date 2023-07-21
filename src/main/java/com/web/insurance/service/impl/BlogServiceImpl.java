package com.web.insurance.service.impl;

import com.web.insurance.mapper.BlogMapper;
import com.web.insurance.mapper.BlogTagMapper;
import com.web.insurance.mapper.CommentMapper;
import com.web.insurance.modelEntity.TypeTops;
import com.web.insurance.po.*;
import com.web.insurance.service.BlogService;
import com.web.insurance.service.TagService;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created on 2020/4/6
 * Package com.web.insurance.service.impl
 *
 * @author dsy
 */
@Service
public class BlogServiceImpl implements BlogService {

    private static final Logger log = LoggerFactory.getLogger(BlogService.class);

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TagService tagService;


    @Override
    public Blog findBlogByBlogId(Integer id) {
        //尝试从redis中获取
            Blog selectBlogByBlogId = blogMapper.selectBlogByBlogId(String.valueOf(id));
            log.info("将blog信息存入redis中，标题为" + selectBlogByBlogId.getTitle());
            return selectBlogByBlogId;
    }

    @Transactional
    @Override
    public Blog addBlog(Blog blog) {
        int flag ;
        //新增
        if (blog.getBlogId()==null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
            flag = blogMapper.insert(blog);
        }else {   //编辑
            blog.setUpdateTime(new Date());
            flag = blogMapper.updateByPrimaryKeySelective(blog);
            //删除原有的Blog对应的标签值
            Example example = new Example(BlogTag.class);
            example.createCriteria().andEqualTo("blogId",blog.getBlogId());
            blogTagMapper.deleteByExample(example);
        }
        return null;
    }

    @Override
    public List<Blog> findAllBlogByPage() {
        return blogMapper.findBlogAll();
    }

    @Transactional
    @Override
    public Integer deleteBlog(Integer id) {
        Blog blog = blogMapper.selectByPrimaryKey(id);
        //删除前将blog_tag表中相关数据删除
        Example exampleBlogTag = new Example(BlogTag.class);
        exampleBlogTag.createCriteria().andEqualTo("blogId", id);
        blogTagMapper.deleteByExample(exampleBlogTag);
        //删除前将comment表中相关数据删除
        Example exampleComment = new Example(Comment.class);
        exampleComment.createCriteria().andEqualTo("blogId", id);
        commentMapper.deleteByExample(exampleComment);
        int delete = blogMapper.deleteByPrimaryKey(id);
        return delete;
    }

    @Override
    public Page<Blog> selectBlogByKeyWords(String title, String typeId, String recommend) {
        List<Blog> blogs = blogMapper.findBlogByKeyWords(title);
        return (Page<Blog>) blogs;
    }

    @Override
    public String findTagsByBlogId(Integer blogId) {
        Example example = new Example(BlogTag.class);
        example.createCriteria().andEqualTo("blogId", blogId);
        List<BlogTag> blogTags = blogTagMapper.selectByExample(example);
        StringBuffer buffer = new StringBuffer();
        for (BlogTag tag : blogTags) {
            buffer.append(tag.getTagId()).append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        return String.valueOf(buffer);
    }

    @Override
    public List<Blog> findTheLastBlog(int i) {
        Example example = new Example(Blog.class);
        example.setOrderByClause("update_time desc limit " + i);
        List<Blog> blogList = blogMapper.selectByExample(example);
        return blogList;
    }

    @Override
    public Page<Blog> findBlogByKeyWords(String key) {
        return (Page<Blog>) blogMapper.findBlogByTitle(key);
    }

    @Override
    public List<Blog> findBlogByTypeId(Integer typeId) {
        List<Blog> blogList = blogMapper.selectBlogByTypeId(typeId);
        for (Blog blog : blogList) {
            List<Tag> tags = tagService.findTagsByBlogId(blog.getBlogId());
            blog.setTags(tags);
        }
        return blogList;
    }

    @Override
    public List<Blog> findBlogByTagId(Integer tagId) {
        List<Blog> blogList = new ArrayList<>();
        Example example = new Example(BlogTag.class);
        example.createCriteria().andEqualTo("TagId", tagId);
        List<BlogTag> blogTags = blogTagMapper.selectByExample(example);
        for (BlogTag blogTag : blogTags) {
            Blog blog = blogMapper.selectBlogByBlogId(String.valueOf(blogTag.getBlogId()));
            List<Tag> tags = tagService.findTagsByBlogId(blog.getBlogId());
            blog.setTags(tags);
            blogList.add(blog);
        }
        return blogList;
    }

    @Override
    public Map<String, List<Blog>> findArchiveBlog() {
        Map<String, List<Blog>> map = new HashMap<>(16);
        List<String> years = blogMapper.findYearsGroupByYear();
        for (String year : years) {
            List<Blog> list = blogMapper.selectBlogByYear(year);
            map.put(year, list);
        }
        return map;
    }

    @Override
    public Integer findBlogCount() {
        return blogMapper.selectCount(null);
    }

    @Override
    public void addBlogViews(Blog blog) {
        blogMapper.updateByPrimaryKeySelective(blog);
        log.info("更新redis中AllBlog的博客标题为：" + blog.getTitle() + "的views为：" + blog.getViews());
    }
}
