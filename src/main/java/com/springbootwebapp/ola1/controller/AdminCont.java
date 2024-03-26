package com.springbootwebapp.ola1.controller;

import com.springbootwebapp.ola1.model.Address;
import com.springbootwebapp.ola1.model.Course;
import com.springbootwebapp.ola1.model.Users;
import com.springbootwebapp.ola1.repository.AddressRepo;
import com.springbootwebapp.ola1.repository.CourseRepo;
import com.springbootwebapp.ola1.repository.UsersRepo;
import com.springbootwebapp.ola1.service.UserService;
import jakarta.validation.Valid;
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
 * Controller class for handling admin-related operations.
 * This class contains methods for managing courses, users, and other admin functionalities.
 *
 */
@Controller
@RequestMapping("/admin")

public class AdminCont {


    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    CourseRepo courseRepo;

    @Autowired
    UserService userService;

    @Autowired
    AddressRepo addressRepo;



    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/home")
    public String adminHome(Model model, Principal principal){

        List<Course> courses = courseRepo.findAll();
        Optional<Users> user = usersRepo.findByEmail(principal.getName());


        List<Users> students = userService.getStudents();
        //List<Users> students = usersRepo.findByRole("ROLE_STUDENT");
        List<Users> instructors = userService.getInstructors();
        //List<Users> instructors = usersRepo.findByRole("ROLE_INSTRUCTOR");

        model.addAttribute("username",principal.getName());
        model.addAttribute("course",courses);
        model.addAttribute("students",students);
        model.addAttribute("instructors",instructors);
        model.addAttribute("totalcourses",courseRepo.count());
        model.addAttribute("totalstudents",students.size());
        model.addAttribute("totalinstructors",instructors.size());
        model.addAttribute("user",user);

        return "admin/home";
    }

    @GetMapping("/profile")
    public String adminProfile(@RequestParam Long id , Model model){

        Optional<Users> user = usersRepo.findById(id);
        user.get().setPassword("");
        model.addAttribute("user",user);
        return "admin/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute Users user, Model model, RedirectAttributes attributes) {

        Users olduser = usersRepo.findById(user.getId()).get();

       // Users fromdb = usersRepo.findByEmail(user.getEmail()).get();

        if (usersRepo.findByEmail(user.getEmail()).isPresent() && !Objects.equals(olduser.getEmail(), user.getEmail())) {
            System.out.println("--------User used existing email-----");
            user.setPassword("");
            attributes.addAttribute("id",user.getId());
            model.addAttribute("user", user);
            return "redirect:/admin/profile?error";
        }
        else {

            System.out.println("User updated successfully");

            olduser.setFirstName(user.getFirstName());
            olduser.setLastName(user.getLastName());
            olduser.setEmail(user.getEmail());
            olduser.setPhoneNumber(user.getPhoneNumber());

            Address address = olduser.getAddress();

            if(address == (null))
                address = new Address();

            address.setCountry(user.getAddress().getCountry());
            address.setState(user.getAddress().getState());
            address.setCity(user.getAddress().getCity());

            addressRepo.save(address);

            olduser.setAddress(address);

            if (!user.getPassword().isEmpty()) {
                olduser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            usersRepo.save(olduser);
            return "redirect:/admin/home?updated";
        }
    }

    @GetMapping("/instructors")
    public String manageInstructors(Model model,Principal principal){

        List<Users> instructor = userService.getInstructors();
        Optional<Users> user = usersRepo.findByEmail(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("instructor",instructor);

        return "admin/instructors";
    }

    @GetMapping("/addInstructor")
    public String showAddInstructor(Model model,Principal principal){
        List<Course> courses = courseRepo.findAll();
        Users newUser = new Users();
        Optional<Users> user = usersRepo.findByEmail(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("courses",courses);
        model.addAttribute("newUser",newUser);
        return "admin/add_instructor";
    }

   /* @GetMapping("/edit-instructor")
    public String updateInstructor(@RequestParam Long id, Model model){
        Course course = courseRepo.findById(id).get();
        model.addAttribute("course",course);
        return "admin/edit-instructor";
    }
*/
    @PostMapping("/instructor/save")
    public String saveInstructor(@Valid @ModelAttribute Users instructor, Model model , Principal principal){

        if(usersRepo.findByEmail(instructor.getEmail()).isPresent()){
            List<Course> courses = courseRepo.findAll();
            Optional<Users> user = usersRepo.findByEmail(principal.getName());
            model.addAttribute("user",user);
            model.addAttribute("error","There is already an account registered with the same email");
            model.addAttribute("courses",courses);
            model.addAttribute("newUser",instructor);
            return "admin/add_instructor";
        }

        instructor.setPassword(passwordEncoder.encode(instructor.getPassword()));
        instructor.setRole("ROLE_INSTRUCTOR");

        /*System.out.println( "Instructor -----------\n"+instructor.toString()+"\n ----------------------");*/

        usersRepo.save(instructor);
        return "redirect:/admin/instructors?updated";
    }

    @GetMapping("/deleteInstructor")
    public String deleteInstructor(@RequestParam Long id, Model model, Principal principal){

        userService.deleteUser(id);
        List<Users> instructor = userService.getInstructors();
        Optional<Users> user = usersRepo.findByEmail(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("instructor",instructor);
        return "redirect:/admin/instructors?deleted";
    }

    @GetMapping("/courses")
    public String manageCourses(Model model, Principal principal){

        List<Course> course = courseRepo.findAll();
        Optional<Users> user = usersRepo.findByEmail(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("course",course);

        return "admin/courses";
    }

    @GetMapping("/addCourse")
    public String showAddCourse(Model model, Principal principal){
        Course course = new Course();
        Optional<Users> user = usersRepo.findByEmail(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("course",course);
        return "admin/add_update_course";
    }

    @GetMapping("/editCourse")
    public String updateCourse(@RequestParam Long id, Model model, Principal principal){
        Course course = courseRepo.findById(id).get();
        Optional<Users> user = usersRepo.findByEmail(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("course",course);
        return "admin/add_update_course";
    }

    @PostMapping("/course/save")
    public String saveCourse(@ModelAttribute Course course){
        courseRepo.save(course);
        return "redirect:/admin/courses?updated";
    }

    @GetMapping("/students")
    public String viewStudents(Model model , Principal principal){

        List<Users> students = usersRepo.findByRole("ROLE_STUDENT");
        Optional<Users> user = usersRepo.findByEmail(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("students",students);
        return "admin/students";

    }


}
