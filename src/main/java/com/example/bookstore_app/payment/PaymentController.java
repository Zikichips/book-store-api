package com.example.bookstore_app.payment;

import com.example.bookstore_app.purchaseOrder.PurchaseOrderProcessingDTO;
import com.example.bookstore_app.purchaseOrder.PurchaseOrderService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private PurchaseOrderService purchaseOrderService;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public PaymentController(PaymentService paymentService, PurchaseOrderService purchaseOrderService) {
        this.paymentService = paymentService;
        this.purchaseOrderService = purchaseOrderService;
    }

    // This endpoint should be enough if I'm working with a frontend like React.
    // I just need to pass the client secret, and the frontend will use Stripe.js to complete the process
    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, Object>> createPaymentIntent() throws StripeException {
        return paymentService.createPaymentIntent();
    }

// This endpoint is only necessary because I'm doing everything through REST API.
// It's not necessary if using a frontend like React
    @PostMapping("/confirm-payment/{paymentIntentId}")
    public ResponseEntity<String> confirmPayment(@PathVariable String paymentIntentId, @RequestBody PurchaseOrderProcessingDTO purchaseOrderProcessingDTO) {
            try {
                // Set your Stripe API key
                Stripe.apiKey = stripeApiKey;

                // Retrieve the PaymentIntent
                PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

                // Confirm the PaymentIntent with the predefined payment method ID
                Map<String, Object> confirmParams = new HashMap<>();
                confirmParams.put("payment_method", "pm_card_visa");  // Using a test payment method

                paymentIntent = paymentIntent.confirm(confirmParams);

                if ("succeeded".equals(paymentIntent.getStatus())) {
                    purchaseOrderService.createOrder(purchaseOrderProcessingDTO.getAddress());
                    return ResponseEntity.ok("Payment successful");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed");
                }
            } catch (StripeException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }







//        try {
//            Stripe.apiKey = stripeApiKey;
//
//            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
//
//            if ("succeeded".equals(paymentIntent.getStatus())) {
//
//                // process Purchase Order Method
////                purchaseOrderService.createOrder(purchaseOrderProcessingDTO.getAddress());
//                return ResponseEntity.ok("Payment successful!");
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed.");
//            }
//        } catch (StripeException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
}
