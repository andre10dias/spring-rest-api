package com.github.andre10dias.spring_rest_api.repository;

import com.github.andre10dias.spring_rest_api.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id = :id")
    void disablePersonById(@Param("id") Long id);

    // Não precisa do @Modifying, por se tratar apenas de uma consulta
    @Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT('%', :firstName, '%'))")
    Page<Person> findPeopleByFirstName(@Param("firstName") String firstName, Pageable pageable);

    @EntityGraph(attributePaths = "books")
    @Query("SELECT p FROM Person p WHERE p.id = :id")
    Optional<Person> findByIdWithBooksGraph(@Param("id") Long id);

}
