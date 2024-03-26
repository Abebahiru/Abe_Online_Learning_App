package com.springbootwebapp.ola1.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Model Entity for Course
 *
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String category;

    private Long duration;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id")
    private List<Review> reviewList;


    public Course(String name, String category, Long duration) {
        this.name = name;
        this.category = category;
        this.duration = duration;
    }

    @Override
    public String toString(){
      return "Id "+id+" Name "+name+" Description "+description+" Category "+category+" Duration "+duration;
    }

}
