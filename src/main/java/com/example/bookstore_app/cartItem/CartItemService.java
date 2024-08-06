package com.example.bookstore_app.cartItem;

import com.example.bookstore_app.book.Book;
import com.example.bookstore_app.book.BookService;
import com.example.bookstore_app.cart.Cart;
import com.example.bookstore_app.cart.CartService;
import com.example.bookstore_app.user.User;
import com.example.bookstore_app.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private BookService bookService;
    private UserService userService;
    private CartService cartService;
    private CartItemRepository cartItemRepository;

    public CartItemService(BookService bookService, UserService userService, CartService cartService, CartItemRepository cartItemRepository) {
        this.bookService = bookService;
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
    }

    public boolean addToCart(Long bookId, Long quantity) {

        Book book = bookService.getBookById(bookId);
        if(book != null) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            CartItem cartItem = new CartItem();
            User user = userService.findByUsername(userDetails.getUsername());
            Cart cart = cartService.findCartByUser(user);
            cartItem.setQuantity(quantity);
            cartItem.setBook(book);
            cartItem.setCart(cart);
            cartItemRepository.save(cartItem);
            return true;
        }

        return false;
    }

    public boolean deleteCartItemById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElse(null);
        if(cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public CartItem findById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElse(null);
        if(cartItem != null) {
            return cartItem;
        }
        return null;
    }

    public boolean updateCartItem(Long id, Long quantity) {
        CartItem cartItem = cartItemRepository.findById(id).orElse(null);
        if(cartItem != null) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return true;
        }
        return false;
    }
}
