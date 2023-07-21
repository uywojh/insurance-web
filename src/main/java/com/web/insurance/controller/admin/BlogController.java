package com.web.insurance.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.web.insurance.po.Blog;
import com.web.insurance.po.User;
import com.web.insurance.service.BlogService;
import com.web.insurance.util.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created on 2020/4/4
 * Package com.web.insurance.controller.admin
 *
 * @author dsy
 */
@Controller
@RequestMapping(value = "/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT = "redirect:/admin/blogs";


    @Autowired
    private BlogService blogService;


    @GetMapping("blogs")
    public String blogs(Model model, @RequestParam(required = false, defaultValue = "1") String page) {
        PageHelper.startPage(Integer.parseInt(page), 8);
        List<Blog> allBlogByPage = blogService.findAllBlogByPage();
        PageInfo<Blog> pageInfo = new PageInfo<>(allBlogByPage);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("flag", 1);
        return LIST;
    }

    @PostMapping("blogs/search")
    public String search(Model model, @RequestParam(required = false, defaultValue = "1") String page,
                         String title) {
        PageHelper.startPage(Integer.parseInt(page), 8);
        Page<Blog> blogPage = blogService.findBlogByKeyWords(title);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogPage);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/blogs :: blogList";
    }

    /**
     * 跳转增加博客页面
     *
     * @param model
     * @return
     */
    @GetMapping("blogs/addPage")
    public String addPage(Model model) {
        model.addAttribute("blog", new Blog());
        return INPUT;
    }


    /**
     * 跳转到修改博客页面
     *
     * @param model
     * @return
     */
    @GetMapping("blogs/{id}/edit")
    public String editPage(Model model, @PathVariable String id) {
        Blog blog = blogService.findBlogByBlogId(Integer.valueOf(id));
        if (blog == null) {
            return "/error/404";
        }
        model.addAttribute("blog", blog);
        return INPUT;
    }

    /**
     * 删除博客
     *
     * @param id 博客id
     * @return
     */
    @GetMapping(value = "blogs/{id}/delete")
    public String deleteBlog(@PathVariable String id, RedirectAttributes attributes) {
        Integer integer = blogService.deleteBlog(Integer.valueOf(id));
        if (integer == 1) {
            attributes.addFlashAttribute("message", "删除成功");
        } else {
            attributes.addFlashAttribute("message", "删除失败");
        }
        return REDIRECT;

    }


    /**
     * 添加(修改并用)博客
     *
     * @param blog       封装博客属性
     * @param session    session
     * @param attributes 用于重定向设置属性，前端获取
     * @param tagIds     博客有关的标签
     * @return
     */
    @PostMapping(value = "/blogs/add")
    public String blogAdd(Blog blog, HttpSession session, RedirectAttributes attributes, String tagIds) {
        User user = (User) session.getAttribute("user");
        blog.setUser(user);
        blog.setRecommend(blog.getRecommend() != null);
        blog.setAppreciation(blog.getAppreciation() != null);
        blog.setShareStatement(blog.getShareStatement() != null);
        blog.setComment(blog.getComment() != null);
        blog.setUserId(user.getUserId());
        blog.setTypeId(blog.getTypeId());
        Blog addBlog = blogService.addBlog(blog);
        if (addBlog != null) {
            attributes.addFlashAttribute("message", "操作成功");
        } else {
            attributes.addFlashAttribute("message", "操作失败");
        }
        return "redirect:/admin/blogs";
    }


    @PostMapping(value = "/blogs/upload")
    public String upload(@RequestParam("insuranceImage") MultipartFile imgFile,HttpSession session, RedirectAttributes attributes) {
        String filename = imgFile.getOriginalFilename();
        File fileDir = UploadUtils.getImgDirFile();
        try {
            File newFile = new File(fileDir.getAbsolutePath() + File.separator + filename);
            imgFile.transferTo(newFile);
            Blog blog = new Blog();
            blog.setTitle(filename.substring(0, filename.lastIndexOf('.')));
            blog.setFirstPicture("/upload/imgs/"+filename);
            blogService.addBlog(blog);
            attributes.addFlashAttribute("message", "操作成功");
        } catch (IOException e) {
            attributes.addFlashAttribute("message", "操作失败");
            e.printStackTrace();
        }
        return "redirect:/admin/blogs";
    }

}
