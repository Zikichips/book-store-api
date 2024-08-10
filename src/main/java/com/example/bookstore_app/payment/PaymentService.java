package com.example.bookstore_app.payment;

import com.example.bookstore_app.cart.Cart;
import com.example.bookstore_app.cart.CartService;
import com.example.bookstore_app.user.User;
import com.example.bookstore_app.user.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private UserService userService;
    private CartService cartService;

    public PaymentService(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public ResponseEntity<Map<String, Object>> createPaymentIntent() throws StripeException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        Cart cart = cartService.findCartByUser(user);

        try {
            Stripe.apiKey = stripeApiKey;

            Map<String, Object> params = new HashMap<>();
            params.put("amount", cart.getTotal_price() * 100);
            params.put("currency", "USD");

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clientSecret", paymentIntent.getClientSecret());


            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


//        PaymentIntentCreateParams params =
//                PaymentIntentCreateParams.builder()
//                        .setAmount(amount * 100) // Amount in cents
//                        .setCurrency(currency)
//                        .build();
//
//        return PaymentIntent.create(params);
    }

}
