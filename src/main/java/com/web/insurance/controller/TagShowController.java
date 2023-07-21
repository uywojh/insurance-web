package com.web.insurance.controller;

import com.web.insurance.modelEntity.TagTops;
import com.web.insurance.po.Blog;
import com.web.insurance.service.BlogService;
import com.web.insurance.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created on 2020/4/10
 * Package com.web.insurance.controller
 *
 * @author dsy
 */
@Controller
public class TagShowController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    @GetMapping(value = "/tags/{tagId}")
    public String tags(@PathVariable Integer tagId, Model model,
                       @RequestParam(value = "page", required = false, defaultValue = "1") String page) {

        Integer count = tagService.findCount();
        List<TagTops> tags = tagService.findSeveralTopTags(count);
        model.addAttribute("tags", tags);
        if (tagId == -1) {
            tagId = tags.get(0).getTagId();
        }
        PageHelper.startPage(Integer.parseInt(page), 5);
        List<Blog> blogList = blogService.findBlogByTagId(tagId);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("activeTagId", tagId);
        return "tags";
    }
}
