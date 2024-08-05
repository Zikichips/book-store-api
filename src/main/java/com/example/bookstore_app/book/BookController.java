package com.example.bookstore_app.book;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks() {
        return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if(book.getId() != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/books")
    public ResponseEntity<String> createBook(@RequestBody Book book) {
        boolean created = bookService.createBook(book);
        if(created) {
            return new ResponseEntity<>("Book created successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Book could not be created", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody Book book) {
        boolean updated = bookService.updateBookById(id, book);
        if(updated) {
            return new ResponseEntity<>("Book updated successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Book could not be updated", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        boolean deleted = bookService.deleteBookById(id);

        if(deleted) {
            return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book could not be deleted", HttpStatus.BAD_REQUEST);
    }


}
