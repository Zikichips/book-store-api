package com.example.bookstore_app.cartItem;

import com.example.bookstore_app.book.Book;
import com.example.bookstore_app.book.BookService;
import com.example.bookstore_app.cart.Cart;
import com.example.bookstore_app.cart.CartRepository;
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
    private CartRepository cartRepository;

    public CartItemService(BookService bookService, UserService userService, CartService cartService, CartItemRepository cartItemRepository, CartRepository cartRepository) {
        this.bookService = bookService;
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
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
            updateCartTotalPrice();
            return true;
        }

        return false;
    }

    public boolean deleteCartItemById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElse(null);
        if(cartItem != null) {
            updateCartTotalPriceBeforeDeletion(cartItem);
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
            updateCartTotalPrice();
            return true;
        }
        return false;
    }

    public void updateCartTotalPrice() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        Cart cart = cartService.findCartByUser(user);

        long updatedPrice = cart.getCartItems().stream()
                .mapToLong(cartItem -> cartItem.getQuantity() * cartItem.getBook().getPrice())
                .sum();
        cart.setTotal_price(updatedPrice);
        cartRepository.save(cart);
    }

    private void updateCartTotalPriceBeforeDeletion(CartItem cartItem) {
        Cart cart = cartItem.getCart();
        long updatedPrice = cart.getCartItems().stream()
                .filter(item -> !item.equals(cartItem))
                .mapToLong(item -> item.getQuantity() * item.getBook().getPrice())
                .sum();
        cart.setTotal_price(updatedPrice);
        cartRepository.save(cart);
    }
}
