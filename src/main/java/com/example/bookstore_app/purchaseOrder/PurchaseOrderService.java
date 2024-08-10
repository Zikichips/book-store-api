package com.example.bookstore_app.purchaseOrder;

import com.example.bookstore_app.cart.Cart;
import com.example.bookstore_app.cart.CartService;
import com.example.bookstore_app.cartItem.CartItemService;
import com.example.bookstore_app.orderItem.OrderItem;
import com.example.bookstore_app.orderItem.OrderItemService;
import com.example.bookstore_app.user.User;
import com.example.bookstore_app.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseOrderService {
    private CartService cartService;
    private UserService userService;
    private PurchaseOrderRepository purchaseOrderRepository;
    private OrderItemService orderItemService;
    private CartItemService cartItemService;

    public PurchaseOrderService(CartService cartService, UserService userService, PurchaseOrderRepository purchaseOrderRepository, OrderItemService orderItemService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.userService = userService;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderItemService = orderItemService;
        this.cartItemService = cartItemService;

    }

    @Transactional
    public void createOrder(String address) {
        // Get cart associated with the authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        Cart cart = cartService.findCartByUser(user);

        // Work on Stripe integration

        // create order
        PurchaseOrder newPurchaseOrder = new PurchaseOrder();
        newPurchaseOrder.setUser(user);
        newPurchaseOrder.setShippingAddress(address);
        newPurchaseOrder.setOrderDate(LocalDate.now());
        newPurchaseOrder.setTotalAmount(cart.getTotal_price());
        newPurchaseOrder.setPaymentStatus(PaymentStatus.PENDING);
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(newPurchaseOrder);

        //check if cart is initially empty
        if(cart.getCartItems().isEmpty()) {
            new ResponseEntity<>("Order could not be created", HttpStatus.BAD_REQUEST);
            return;
        }
        // Populate order items with cart items
        if(savedPurchaseOrder.getId() != null) {

            List<OrderItem> orderItems = new ArrayList<>();
            cart.getCartItems().forEach(cartItem -> {
                OrderItem orderItem = orderItemService.createOrderItem(cartItem, savedPurchaseOrder);
                orderItems.add(orderItem);
            });
            savedPurchaseOrder.setOrderItems(orderItems);
            purchaseOrderRepository.save(savedPurchaseOrder);


            // update payment status
            savedPurchaseOrder.setPaymentStatus(PaymentStatus.PAID);
            purchaseOrderRepository.save(savedPurchaseOrder);

            // empty cart if successful

            cart.getCartItems().removeAll(cart.getCartItems());

            boolean cartEmpty = cartService.clearCart(cart);
            cartItemService.updateCartTotalPrice();
            if(cartEmpty) {
                savedPurchaseOrder.setPaymentStatus(PaymentStatus.COMPLETED);
                purchaseOrderRepository.save(savedPurchaseOrder);
                new ResponseEntity<>("Order created successfully", HttpStatus.OK);
                return;

            }
            new ResponseEntity<>("Order could not be created", HttpStatus.BAD_REQUEST);
            return;
        }

        new ResponseEntity<>("Order could not be created", HttpStatus.BAD_REQUEST);
    }

    public List<PurchaseOrder> getAllUserOrders() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        return user.getOrders();
    }

    public PurchaseOrder getOrderById(Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        PurchaseOrder purchaseOrder = user.getOrders().stream()
                .filter(order -> order.getId() == id)
                .findFirst().orElse(null);
        if(purchaseOrder.getId() != null) {
            return purchaseOrder;
        }
        return null;
    }
}
