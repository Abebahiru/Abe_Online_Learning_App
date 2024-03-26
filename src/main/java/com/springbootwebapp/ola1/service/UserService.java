package com.springbootwebapp.ola1.service;

import com.springbootwebapp.ola1.model.Course;
import com.springbootwebapp.ola1.model.Users;
import com.springbootwebapp.ola1.repository.CourseRepo;
import com.springbootwebapp.ola1.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    CourseRepo courseRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Users addNewUser(Users users){
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return usersRepo.save(users);
    }
    public List<Users> getAllUsers(){
        return usersRepo.findAll();
    }

    public List<Users> getStudents(){
        return usersRepo.findByRole("ROLE_STUDENT");
    }
    public List<Users> getInstructors(){
        return usersRepo.findByRole("ROLE_INSTRUCTOR");
    }

    public Long numberOfUsers(){
        return usersRepo.count();
    }

    public Optional<Users> getUserByEmail(String email) {
        return usersRepo.findByEmail(email);
    }

    public Boolean enrollCourse(Long userId, Course course){

        try {
            Users user = usersRepo.findById(userId).get();

            Collection<Course> courses = user.getCourses();
            courses.add(course);
            user.setCourses(courses);
            usersRepo.save(user);
            return true;

        }catch (Exception e){
            e.getLocalizedMessage();
            return false;
        }

    }

    public void deleteUser(Long id){
        Users user = usersRepo.findById(id).get();
        user.setCourses(null);
        usersRepo.save(user);
        usersRepo.deleteById(id);
    }

}
