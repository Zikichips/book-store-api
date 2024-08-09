package com.example.bookstore_app.cart;

import com.example.bookstore_app.user.User;
import com.example.bookstore_app.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private CartRepository cartRepository;
    private UserService userService;

    public CartService(CartRepository cartRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    public void createCart(String username) {
        User user = userService.findByUsername(username);
        Cart cart = new Cart();
        cart.setTotal_price(0L);
        cart.setUser(user);
        cartRepository.save(cart);


    }

    public Cart findCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    @Transactional
    public boolean clearCart(Cart cart) {
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return true;
    }
}
