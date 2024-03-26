package com.springbootwebapp.ola1.service;

import com.springbootwebapp.ola1.model.Course;
import com.springbootwebapp.ola1.repository.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

        @Autowired
        private CourseRepo courseRepo;

     public Course newCourse(Course course){
         try {
             return courseRepo.save(course);
         }catch (Exception e){
             e.getLocalizedMessage();
             return null;
         }
     }

     public List<Course> getAllCourse(){
         try {
             return courseRepo.findAll();
         }catch (Exception e){
             e.getLocalizedMessage();
             e.printStackTrace();
             return  null;
         }
     }

     public Course getById(Long id){
         return courseRepo.findById(id).get();
     }

     public List<Course> getOtherCourses(Long studentId){
         return courseRepo.otherCourses(studentId);
     }
}
