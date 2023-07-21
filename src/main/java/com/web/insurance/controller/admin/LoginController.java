package com.web.insurance.controller.admin;

import com.web.insurance.po.User;
import com.web.insurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

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

    @RequestMapping
    public String loginPage() {
        return "admin/login";
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
