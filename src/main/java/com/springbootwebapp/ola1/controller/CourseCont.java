package com.springbootwebapp.ola1.controller;

import com.springbootwebapp.ola1.model.Course;
import com.springbootwebapp.ola1.repository.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Controller class for handling course-related operations.
 * This class contains methods for managing adding, updating, listing courses.
 *
 */
@Controller
@RequestMapping("/api/course")
public class CourseCont {

    @Autowired
    private CourseRepo courseRepo;

    @GetMapping("")
    public ResponseEntity<List<Course>> getAllCourses(@RequestParam(required = false) String category){
        List<Course> courses;

        //System.out.println(category);

        if(category != null){
            courses = courseRepo.findByCategory(category);
        }
        else {
            courses = courseRepo.findAll();
        }


        if(courses.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(courses,HttpStatus.OK);
        }
    }
/*

    @GetMapping("/test")
    public ResponseEntity<List<Course>> byDuration(){
        try{

            List<Course> courses = courseRepo.findByDurationMore(11L);
            return new ResponseEntity<>(courses,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            e.getLocalizedMessage();
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mine/{id}")
    public ResponseEntity<List<Course>> findMyCourses(@PathVariable Long id){
        try {
            List<Course> myCourses = courseRepo.myCourses(id);
            if(myCourses.isEmpty()){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }else
                return new ResponseEntity<>(myCourses,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            e.getLocalizedMessage();
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/total")
    public ResponseEntity<String> numberOfCourses(){
        try {
            long total = courseRepo.count();
            return new ResponseEntity<>("Total = "+total,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
*/


    @GetMapping("/web")
    public String getCourses(Model model){
        List<Course> courses = courseRepo.findAll();
        model.addAttribute("course",courses);
        model.addAttribute("totalcourses",courseRepo.count());
        return "courses/list-course";

    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Course>> getCourseById(@PathVariable Long id){
        Optional<Course> course = courseRepo.findById(id);
        if(course.isPresent()) {
            return new ResponseEntity<>(course,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("web/course/show-add-form")
    public String showAddForm(Model model){
        Course course = new Course();
        model.addAttribute("course",course);
        return "add-course";
    }



    @PostMapping("")
    public ResponseEntity<Course> addNewCourse(@RequestBody Course course){
        try{
            return new ResponseEntity<>(courseRepo.save(course),HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
