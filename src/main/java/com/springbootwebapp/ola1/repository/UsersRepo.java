package com.springbootwebapp.ola1.repository;

import com.springbootwebapp.ola1.model.Course;
import com.springbootwebapp.ola1.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users,Long> {
     Optional<Users> findByEmail(String email);

     List<Users> findByRole(String role);

     @Query(value = "select * from users where role=\"ROLE_STUDENT\" and id in (select user_id from users_courses where course_id = :course_id);",nativeQuery = true)
     List<Users> enrolledCourse(@Param("course_id") Long id);

   //  Users findByRole(String role);
}
