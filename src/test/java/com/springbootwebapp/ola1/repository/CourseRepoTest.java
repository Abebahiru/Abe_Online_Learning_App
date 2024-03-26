package com.springbootwebapp.ola1.repository;

import com.springbootwebapp.ola1.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CourseRepoTest {

    @Autowired
    private CourseRepo courseRepo;



    @Test
    public void testFindByCategory() {
        Course course1 = new Course("Java Programming", "Programming", 30L);
        Course course2 = new Course("Web Development", "Web Development", 45L);

        courseRepo.save(course1);
        courseRepo.save(course2);

        List<Course> programmingCourses = courseRepo.findByCategory("Programming");

        assertEquals("Java Programming", programmingCourses.get(0).getName());
    }


}
