package com.example.bookstore_app.purchaseOrder;

import com.example.bookstore_app.cart.Cart;
import com.example.bookstore_app.cart.CartService;
import com.example.bookstore_app.cartItem.CartItemService;
import com.example.bookstore_app.orderItem.OrderItem;
import com.example.bookstore_app.orderItem.OrderItemService;
import com.example.bookstore_app.user.User;
import com.example.bookstore_app.user.UserService;
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
    public boolean createOrder(PurchaseOrderProcessingDTO shippingAddress) {
        // Get cart associated with the authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        Cart cart = cartService.findCartByUser(user);

        // Work on Stripe integration

        // create order
        PurchaseOrder newPurchaseOrder = new PurchaseOrder();
        newPurchaseOrder.setUser(user);
        newPurchaseOrder.setShippingAddress(shippingAddress.getShippingAddress());
        newPurchaseOrder.setOrderDate(LocalDate.now());
        newPurchaseOrder.setTotalAmount(cart.getTotal_price());
        newPurchaseOrder.setPaymentStatus(PaymentStatus.PENDING);
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(newPurchaseOrder);

        System.out.println("Order Created - Status Pending");

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

            System.out.println("Order paid - Status Paid");

            // empty cart if successful

            cart.getCartItems().removeAll(cart.getCartItems());

//            List<CartItem> cartItemsToDelete = cart.getCartItems().stream()
//                    .filter(cartItem -> cartItemService.findById(cartItem.getId()))
//                    .collect(Collectors.toList());
////            cart.getCartItems().forEach(cartItem -> cartItemService.deleteCartItemById(cartItem.getId()));
//            boolean cartItemsDeleted = cart.getCartItems().stream()
//                            .map(cartItem -> cartItemService.deleteCartItemById(cartItem.getId()))
//                    .allMatch(deleted -> deleted);

            System.out.println("CartItems cleared");
            boolean cartEmpty = cartService.clearCart(cart);
            cartItemService.updateCartTotalPrice();
            if(cartEmpty) {
                savedPurchaseOrder.setPaymentStatus(PaymentStatus.COMPLETED);
                purchaseOrderRepository.save(savedPurchaseOrder);
                System.out.println("Cart cleared - Status Completed");
                return true;
            }
            return false;
        }

        return false;
    }
}
