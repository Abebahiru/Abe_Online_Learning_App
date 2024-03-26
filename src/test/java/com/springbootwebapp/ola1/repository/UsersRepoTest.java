package com.springbootwebapp.ola1.repository;

import com.springbootwebapp.ola1.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsersRepoTest {

    @Autowired
    private UsersRepo usersRepo;


    @Test
    public void testFindByEmail() {
        String email = "test@example.com";
        Users user = new Users();
        user.setEmail(email);
        user.setPassword("password");

        usersRepo.save(user);

        Optional<Users> foundUser = usersRepo.findByEmail(email);

        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());
    }

    @Test
    public void testFindByRole() {
        String role = "ROLE_ADMIN";
        Users user1 = new Users();
        user1.setRole(role);
        user1.setEmail("email1");
        user1.setPassword("password");

        Users user2 = new Users();
        user2.setRole(role);
        user2.setEmail("email2");
        user2.setPassword("password");

        usersRepo.save(user1);
        usersRepo.save(user2);

        List<Users> foundUsers = usersRepo.findByRole(role);

        assertEquals(3, foundUsers.size());
        for (Users user : foundUsers) {
            assertEquals(role, user.getRole());
        }
    }

}