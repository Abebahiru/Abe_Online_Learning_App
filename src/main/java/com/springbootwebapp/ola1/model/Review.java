package com.springbootwebapp.ola1.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Model Entity for Review Courses
 *
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rating;

    private String feedback;
}
