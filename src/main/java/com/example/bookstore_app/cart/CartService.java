package com.example.bookstore_app.cart;

import com.example.bookstore_app.user.User;
import com.example.bookstore_app.user.UserService;
import org.springframework.stereotype.Service;

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
        cart.setTotal_price(0);
        cart.setUser(user);
        cartRepository.save(cart);


    }

    public Cart findCartByUser(User user) {
        return cartRepository.findByUser(user);
    }
}
