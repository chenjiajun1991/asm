package com.sam.yh.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sam.yh.model.UserExample;
import com.sam.yh.service.UserExampleService;

/**
 * @author nate
 */
@Controller
@RequestMapping("/user")
public class UserExampleController {
    @Resource
    private UserExampleService userExampleService;

    @RequestMapping("/showUser")
    public String toIndex(HttpServletRequest request, Model model) {
        int userId = Integer.parseInt(request.getParameter("id"));
        UserExample user = this.userExampleService.getUserById(userId);
        model.addAttribute("user", user);
        return "showUser";
    }

}
