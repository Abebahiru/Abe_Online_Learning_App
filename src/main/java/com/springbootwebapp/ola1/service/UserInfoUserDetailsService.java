package com.springbootwebapp.ola1.service;

import com.springbootwebapp.ola1.config.UserInfoUserDetails;
import com.springbootwebapp.ola1.model.Users;
import com.springbootwebapp.ola1.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *  UserDetailsService implementation for retrieving user information.
 *  This class is responsible for loading user-specific data during authentication.
 *
 */
@Service
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    UsersRepo usersRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Users> users = usersRepo.findByEmail(username);

        return users.map(UserInfoUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("User "+username+" Not Found"));

    }
}
