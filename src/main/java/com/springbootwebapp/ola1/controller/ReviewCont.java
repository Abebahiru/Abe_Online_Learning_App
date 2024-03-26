package com.springbootwebapp.ola1.controller;

import com.springbootwebapp.ola1.model.Course;
import com.springbootwebapp.ola1.model.Review;
import com.springbootwebapp.ola1.repository.CourseRepo;
import com.springbootwebapp.ola1.repository.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/review")
public class ReviewCont {

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    CourseRepo courseRepo;

    @GetMapping("")
    public ResponseEntity<List<Review>> getAll(){
        return new ResponseEntity<>(reviewRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Review> addReview(@RequestBody Review review){
/*
        if(courseRepo.existsById(review.getCourse().toString())) {
            Course course = courseRepo.findById(review.getId()).get();

            review.setCourse(course);
        }*/
        return new ResponseEntity<>(reviewRepo.save(review),HttpStatus.OK);
    }


}
