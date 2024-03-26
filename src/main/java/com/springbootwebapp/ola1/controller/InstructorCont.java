package com.springbootwebapp.ola1.controller;

import com.springbootwebapp.ola1.model.Users;
import com.springbootwebapp.ola1.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller class for handling instructor-related operations.
 * This class contains methods for viewing courses, users, and other instructor functionalities.
 *
 */

@Controller
@RequestMapping("/instructor")
public class InstructorCont {

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping("/home")
    public String InstructorHome(Model model, Principal principal){
        Users user = usersRepo.findByEmail(principal.getName()).get();

        model.addAttribute("user",user);
        return "instructor/home";
    }

    @GetMapping("/profile")
    public String studentProfile(@RequestParam Long id , Model model){

        Users user = usersRepo.findById(id).get();
        user.setPassword("");
        model.addAttribute("user",user);
        return "instructor/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute Users user, Model model, RedirectAttributes attributes) {

        Users olduser = usersRepo.findById(user.getId()).get();
        if (usersRepo.findByEmail(user.getEmail()).isPresent() && !Objects.equals(olduser.getEmail(), user.getEmail())) {
            user.setPassword("");
            attributes.addAttribute("id",user.getId());
            model.addAttribute("user", user);
            return "redirect:/instructor/profile?error";
        }
        else {

            olduser.setFirstName(user.getFirstName());
            olduser.setLastName(user.getLastName());
            olduser.setEmail(user.getEmail());
            olduser.setPhoneNumber(user.getPhoneNumber());
            if (!user.getPassword().isEmpty()) {
                olduser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            usersRepo.save(olduser);
            return "redirect:/instructor/home?updated";
        }
    }

    @GetMapping("/students")
    public String myStudents(@RequestParam Long course_id, Model model, Principal principal){
        List<Users> students = usersRepo.enrolledCourse(course_id);
        Optional<Users> user = usersRepo.findByEmail(principal.getName());
        model.addAttribute("user",user.get());
        model.addAttribute("students",students);
        return "instructor/students";
    }

}
