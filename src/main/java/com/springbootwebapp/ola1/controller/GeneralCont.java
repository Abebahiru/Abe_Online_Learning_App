package com.springbootwebapp.ola1.controller;

import com.springbootwebapp.ola1.model.Users;
import com.springbootwebapp.ola1.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller class for general operations.
 * This class contains methods for login,filtering and other functionalities.
 *
 */

@Controller
public class GeneralCont {

    @Autowired
    private UsersRepo usersRepo;

    @GetMapping("/")
    public String index(){
        return "index";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/filter")
    public String filter(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authority = auth.getAuthorities().toString();
        System.out.println(auth.getAuthorities());
        if(authority.contains("ADMIN")){
            return "redirect:/admin/home";
        } else if (authority.contains("STUDENT")) {
            return "redirect:/student/home";
        } else if (authority.contains("INSTRUCTOR")) {
            return "redirect:/instructor/home";
        }
        else {
            System.out.println(" Authority diff "+authority);
            return "redirect:/course/web";
        }
    }

}
