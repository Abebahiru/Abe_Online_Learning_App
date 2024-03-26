package com.springbootwebapp.ola1.repository;

import com.springbootwebapp.ola1.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<Review,Long> {
}
