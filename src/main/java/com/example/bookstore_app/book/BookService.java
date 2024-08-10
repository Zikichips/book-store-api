package com.example.bookstore_app.book;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public boolean createBook(Book book) {
        Book created = bookRepository.save(book);
        if(created.getId() != null) {
            return true;
        }
        return false;
    }

//    public List<Book> searchBooks(String searchTerm) {
//        return bookRepository.searchBooks(searchTerm);
//    }

    public boolean updateBookById(Long id, Book book) {
        Book oldBook = this.getBookById(id);
        if(oldBook != null) {
            book.setId(oldBook.getId());
            bookRepository.save(book);
            return true;
        }
        return false;
    }

    public boolean deleteBookById(Long id) {
        Book book = this.getBookById(id);
        if(book != null) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
