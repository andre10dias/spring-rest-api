package com.github.andre10dias.spring_rest_api.repository;

import com.github.andre10dias.spring_rest_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
