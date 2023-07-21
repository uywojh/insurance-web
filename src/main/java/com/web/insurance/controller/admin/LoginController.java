package com.web.insurance.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.web.insurance.po.Blog;
import com.web.insurance.po.User;
import com.web.insurance.service.BlogService;
import com.web.insurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created on 2020/4/4
 * Package com.web.insurance.controller.admin
 *
 * @author dsy
 */
@Controller
@RequestMapping(value = "/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    private static final String LIST = "admin/blogs";

    @RequestMapping
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping(value = "login")
    public String login(Model model, @RequestParam String username,
                        @RequestParam String password,@RequestParam(required = false, defaultValue = "1") String page,
                        HttpSession session, RedirectAttributes attributes) {
        User admin = userService.checkUser(username, password);
        if (admin != null) {
            admin.setPassword(null);
            session.setAttribute("user", admin);
            PageHelper.startPage(Integer.parseInt(page), 8);
            List<Blog> allBlogByPage = blogService.findAllBlogByPage();
            PageInfo<Blog> pageInfo = new PageInfo<>(allBlogByPage);
            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute("flag", 1);
            return LIST;
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误");
            return "redirect:admin";
        }
    }

    @GetMapping(value = "/index")
    public String login(HttpSession session) {
        User admin = new User();
        admin.setPassword(null);
        admin.setUsername("User");
        session.setAttribute("user", admin);
        return "admin/index";
    }


    @GetMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:admin";
    }

}
