package com.springbootwebapp.ola1.controller;

import com.springbootwebapp.ola1.model.Address;
import com.springbootwebapp.ola1.model.Users;
import com.springbootwebapp.ola1.repository.AddressRepo;
import com.springbootwebapp.ola1.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for handling user-related operations.
 * This class contains methods for adding , listing, and other operations on   users.
 *
 */

@Controller
@RequestMapping("/api/users")
public class UsersCont {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepo addressRepo;


    @GetMapping("")
    public ResponseEntity<List<Users>> getAllUsers(){
        List<Users> users;
        users = usersRepo.findAll();
        System.out.println(users);
        if(users.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(users,HttpStatus.OK);
        }

    }

    @PostMapping("")
    public ResponseEntity<Users> addUser(@RequestBody Users user){
        try {

            Address address = user.getAddress(); // Get the address from the user
            user.setAddress(null); // Set the address to null in the user object

            // Save the address first
            Address savedAddress = addressRepo.save(address);

            // Set the saved address back to the user object
            user.setAddress(savedAddress);

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return new ResponseEntity<>(usersRepo.save(user),HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            String error = "Error = > "+e.getLocalizedMessage();
            System.out.println(error);
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    


}
