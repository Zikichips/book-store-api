package com.example.bookstore_app.book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Search by title, author, or category
//    @Query("SELECT b FROM Book b WHERE " +
//            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
//            "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
//            "LOWER(CAST(b.category AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
//    List<Book> searchBooks(@Param("searchTerm") String searchTerm);
}
