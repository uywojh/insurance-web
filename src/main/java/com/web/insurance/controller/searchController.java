package com.web.insurance.controller;

import com.web.insurance.po.Blog;
import com.web.insurance.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class searchController {

    @Autowired
    private BlogService blogService;

    /**
     * @param title 关键词
     * @return
     */
    @GetMapping("/blog/search")
    public Blog searchBlogByKeyWords(@RequestParam(value = "title") String title) {
        Blog blog = blogService.findBlogByTitle(title);
        return blog;
    }


}
