package com.springbootwebapp.ola1.repository;

import com.springbootwebapp.ola1.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CourseRepo extends JpaRepository<Course, Long> {

    List<Course> findByCategory(String category);
/*
    @Query(value = "select * from Course where duration >= :dur",nativeQuery = true)
    List<Course> findByDurationMore(@Param("dur") Long duration);*/

    @Query(value = "select * from ola.course where id not in (select course_id from users_courses where user_id =:id)",nativeQuery = true)
    List<Course> otherCourses(@Param("id") Long id);



    /*@Query(value = "INSERT INTO USERS_COURSES (USER_ID, COURSE_ID) VALUES (:user_id, :course_id)" , nativeQuery = true)
    String enroll(@Param("user_id") Long user_id, @Param("course_id") Long course_id);*/

}