package com.example.bookstore_app.cartItem;

import com.example.bookstore_app.cart.CartService;
import com.example.bookstore_app.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartItemController {
    private CartItemService cartItemService;
    private UserService userService;
    private CartService cartService;

    public CartItemController(CartItemService cartItemService, UserService userService, CartService cartService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/cartitem/{id}")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable Long id) {
        CartItem itemExists = cartItemService.findById(id);
        if(itemExists != null) {
            return new ResponseEntity<>(itemExists, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/cartitem")
    public ResponseEntity<String> addToCart(@RequestBody CartDTO cartDTO) {
        boolean addedToCart = cartItemService.addToCart(cartDTO.getBook_id(), cartDTO.getQuantity());
        if(addedToCart) {
            return new ResponseEntity<>("Item added to cart successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Item could not be added to cart", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/cartitem/{id}")
    public ResponseEntity<String> updateCartItem(@PathVariable Long id, @RequestBody CartItemQuantityDTO quantityDTO) {
        boolean updateCartItemQuantity = cartItemService.updateCartItem(id, quantityDTO.getQuantity());
        if(updateCartItemQuantity) {
            return new ResponseEntity<>("Item updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Item could not be updated", HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/cartitem/{id}")
    public ResponseEntity<String> removeItemFromCart(@PathVariable Long id) {
        boolean deleted = cartItemService.deleteCartItemById(id);
        if(deleted) {
            return new ResponseEntity<>("Item deleted from cart", HttpStatus.OK);
        }
        return new ResponseEntity<>("Could not delete item from cart" , HttpStatus.BAD_REQUEST);

    }

}
