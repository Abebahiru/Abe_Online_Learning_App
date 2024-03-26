package com.springbootwebapp.ola1.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Collection;
import java.util.Objects;

/**
 * Model Entity for Users
 *
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "address", referencedColumnName = "id")
    private Address address;

    @Column(nullable = false,unique = true)
    private String email;


    @Column(nullable = false)
    private String password;

    private String role;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_courses",

            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),

            inverseJoinColumns = @JoinColumn(
                    name = "course_id" , referencedColumnName = "id")
    )
    private Collection<Course> courses;



public Users(String firstName, String lastName, String email, String password, String role){
    this.firstName = firstName;
    this.lastName = lastName;
    this.email=email;
    this.password = password;
    this.role=role;
}
public Users(String email, String password, String role){
    this.email=email;
    this.password = password;
    this.role=role;
}

    public Users() {

    }

    @Override
    public String toString(){
    return "Name"+this.firstName+" "+this.lastName+" Pass "+this.password+" Courses "+this.courses.toString();
    }
}
