package org.example.repository;
import org.example.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameAndBirthYearAndDeathYear(String name, Integer birthYear, Integer deathYear);
}
