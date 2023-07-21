package com.web.insurance.controller;

import com.web.insurance.po.Blog;
import com.web.insurance.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * Created on 2020/4/10
 * Package com.web.insurance.controller
 *
 * @author dsy
 */
@Controller
public class ArchivesController {

    @Autowired
    private BlogService blogService;

    @GetMapping(value = "/archives")
    public String archives(Model model) {
        Map<String, List<Blog>> map = blogService.findArchiveBlog();
        model.addAttribute("map", map);
        model.addAttribute("count", blogService.findBlogCount());
        return "archives";
    }

}
