package com.github.andre10dias.spring_rest_api.repository;

import com.github.andre10dias.spring_rest_api.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
