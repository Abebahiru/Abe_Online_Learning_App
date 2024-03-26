package com.springbootwebapp.ola1.service;
import com.springbootwebapp.ola1.model.Users;
import com.springbootwebapp.ola1.repository.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UsersRepo usersRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewUser() {
        // Arrange
        Users user = new Users("John", "Doe", "john.doe@example.com", "password", "ROLE_STUDENT");
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(usersRepo.save(Mockito.any(Users.class))).thenReturn(user);

        // Act
        Users savedUser = userService.addNewUser(user);

        // Assert
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("John", savedUser.getFirstName());
        // Add more assertions as needed
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        List<Users> userList = new ArrayList<>();
        userList.add(new Users("John", "Doe", "john.doe@example.com", "password", "ROLE_STUDENT"));
        userList.add(new Users("Jane", "Smith", "jane.smith@example.com", "password", "ROLE_INSTRUCTOR"));
        when(usersRepo.findAll()).thenReturn(userList);

        // Act
        List<Users> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        // Add more assertions as needed
    }

    @ParameterizedTest
    @CsvSource({
            "john.doe@example.com, John, Doe, password, ROLE_STUDENT",
            "jane.smith@example.com, Jane, Smith, password, ROLE_INSTRUCTOR"
    })
    public void testGetUserByEmail(String email, String firstName, String lastName, String password, String role) {
        // Arrange
        Users user = new Users(firstName, lastName, email, password, role);
        when(usersRepo.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Users foundUser = userService.getUserByEmail(email).orElse(null);

        // Assert
        assert foundUser != null;
        assertEquals(firstName, foundUser.getFirstName());
        assertEquals(lastName, foundUser.getLastName());
        assertEquals(password, foundUser.getPassword());
        assertEquals(role, foundUser.getRole());
    }


    // Add more test methods for other methods in UserService class
}