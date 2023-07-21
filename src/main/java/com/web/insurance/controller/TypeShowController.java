package com.web.insurance.controller;

import com.web.insurance.modelEntity.TypeTops;
import com.web.insurance.po.Blog;
import com.web.insurance.service.BlogService;
import com.web.insurance.service.TypeService;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping(value = "/types/{typeId}")
    public String types(@PathVariable Integer typeId, @RequestParam(value = "page",
            required = false, defaultValue = "1") String page, Model model) {
        Integer count = typeService.findCount();
        List<TypeTops> types = typeService.findSeveralTypes(count);
        model.addAttribute("types", types);
        PageHelper.startPage(Integer.parseInt(page), 5);
        if (typeId == -1) {
            typeId = types.get(0).getTypeId();
        }
        List<Blog> blogList = blogService.findBlogByTypeId(typeId);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("activeTypeId", typeId);
        return "types";
    }
}
