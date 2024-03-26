package com.springbootwebapp.ola1.controller;

import com.springbootwebapp.ola1.model.Course;
import com.springbootwebapp.ola1.model.Users;
import com.springbootwebapp.ola1.repository.CourseRepo;
import com.springbootwebapp.ola1.repository.UsersRepo;
import com.springbootwebapp.ola1.service.CourseService;
import com.springbootwebapp.ola1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller class for handling student-related operations.
 * This class contains methods for viewing courses, enrolling into courses, and other student functionalities.
 *
 */


@Controller
@RequestMapping("/student")
public class StudentCont {

    @Autowired
    UserService userService;

    @Autowired
    UsersRepo usersRepo;

    /*@Autowired
    CourseRepo courseRepo;*/

    @Autowired
    CourseService courseService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String StudentHome(Model model, Principal principal){
        Users user = usersRepo.findByEmail(principal.getName()).get();

        model.addAttribute("user",user);

        return "student/home";
    }

    @GetMapping("/signup")
    public String ShowSignUpForm(Model model){
        Users newStudent = new Users();
        model.addAttribute("user",newStudent);
        return "student/student-signup";
        }

    @PostMapping("/signup/save")
    public String saveStudent(@ModelAttribute Users newStudent, Model model){

        //Users existingUser = null;

        if(usersRepo.findByEmail(newStudent.getEmail()).isPresent()){
            model.addAttribute("user",newStudent);
            model.addAttribute("error","User with the same email already exists!");
            return "student/student-signup";
        }

        newStudent.setRole("ROLE_STUDENT");
        userService.addNewUser(newStudent);
        return "redirect:/login?registered";
    }

    @GetMapping("/courses")
    public String allCourses(@RequestParam Long student_id , Model model, Principal principal){

        //To be changed
        //List<Course> otherCourses = courseRepo.otherCourses(student_id);
        List<Course> otherCourses = courseService.getOtherCourses(student_id);
        Optional<Users> user = usersRepo.findByEmail(principal.getName());
        model.addAttribute("user",user.get());
        model.addAttribute("courses",otherCourses);
        model.addAttribute("student_id",student_id);
        return "student/courses";

    }

    @GetMapping("/enroll")
    public String enroll(@RequestParam("student_id") Long studentId , @RequestParam("course_id") Long course_id, RedirectAttributes attributes){

        Course course = courseService.getById(course_id);
        if(userService.enrollCourse(studentId,course))
            return "redirect:/student/home";
        else{
            attributes.addAttribute("studentId",studentId);
            return  "redirect:/student/courses?error";
        }
    }

    @GetMapping("/profile")
    public String studentProfile(@RequestParam Long id , Model model){

        Users user = usersRepo.findById(id).get();
        user.setPassword("");
        model.addAttribute("user",user);
        return "student/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute Users user, Model model, RedirectAttributes attributes) {

        Users olduser = usersRepo.findById(user.getId()).get();
        if (usersRepo.findByEmail(user.getEmail()).isPresent() && !Objects.equals(olduser.getEmail(), user.getEmail())) {
            System.out.println("--------User used existing email-----");
            user.setPassword("");
            attributes.addAttribute("id",user.getId());
            model.addAttribute("user", user);
            return "redirect:/student/profile?error";
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
            return "redirect:/student/home?updated";
        }
    }


    }

