package com.example.bookstore_app.book;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/api/books")
    public ResponseEntity<List<Book>> getBooks() {
        return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if(book.getId() != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
//        List<Book> books = bookService.searchBooks(query);
//        return ResponseEntity.ok(books);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/api/books")
    public ResponseEntity<String> createBook(@RequestBody Book book) {
        boolean created = bookService.createBook(book);
        if(created) {
            return new ResponseEntity<>("Book created successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Book could not be created", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/api/books/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody Book book) {
        boolean updated = bookService.updateBookById(id, book);
        if(updated) {
            return new ResponseEntity<>("Book updated successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Book could not be updated", HttpStatus.BAD_REQUEST);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/api/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        boolean deleted = bookService.deleteBookById(id);

        if(deleted) {
            return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book could not be deleted", HttpStatus.BAD_REQUEST);
    }


}
