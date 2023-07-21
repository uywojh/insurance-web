package com.web.insurance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created on 2020/4/11
 * Package com.web.insurance.controller
 *
 * @author dsy
 */
@Controller
public class AboutController {

    @GetMapping(value = "/about")
    public String about() {
        return "about";
    }
}
