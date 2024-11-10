package org.example.repository;

import org.example.model.Author;
import org.example.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b ORDER BY b.download_count DESC")
    List<Book> findTop10ByOrderByDownloadCountDesc();

    @Query("SELECT a FROM Author a WHERE a.birthYear >= :startYear AND a.deathYear <= :endYear")
    List<Author> findAuthorsInYearRange(@Param("startYear") int startYear, @Param("endYear") int endYear);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.authors WHERE b.id = :id")
    Book findByIdWithAuthors(@Param("id") Long id);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.authors")
    List<Book> findAllWithAuthors();
}
