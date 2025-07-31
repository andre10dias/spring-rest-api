package com.github.andre10dias.spring_rest_api.repository;

import com.github.andre10dias.spring_rest_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u JOIN FETCH u.permissions WHERE u.username = :username")
    Optional<User> findByUsernameFetchPermissions(@Param("username") String username);



}
