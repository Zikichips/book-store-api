package com.example.bookstore_app.cart;

import com.example.bookstore_app.user.User;
import com.example.bookstore_app.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CartController {
    private CartService cartService;
    private UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/cart")
    public ResponseEntity<Cart> getCartForUser(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails.getUsername() != null) {
            User user = userService.findByUsername(userDetails.getUsername());
            Cart cart = cartService.findCartByUser(user);
            if(cart != null) {
                return new ResponseEntity<>(cart, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

//    @PutMapping("/cart/add")
}
