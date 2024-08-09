package com.example.bookstore_app.orderItem;

import com.example.bookstore_app.cartItem.CartItem;
import com.example.bookstore_app.purchaseOrder.PurchaseOrder;
import com.example.bookstore_app.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    private OrderItemRepository orderItemRepository;
    private UserService userService;

    public OrderItemService(OrderItemRepository orderItemRepository, UserService userService) {
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
    }

    public OrderItem createOrderItem(CartItem cartItem, PurchaseOrder savedPurchaseOrder) {
        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setPrice(cartItem.getBook().getPrice());
        newOrderItem.setQuantity(cartItem.getQuantity());
        newOrderItem.setBook(cartItem.getBook());
        newOrderItem.setOrder(savedPurchaseOrder);
        OrderItem newOrderItemSaved = orderItemRepository.save(newOrderItem);
        if(newOrderItemSaved.getId() != null) {
            return newOrderItemSaved;
        }
        return null;
    }

//    public List<OrderItem> getAllUserOrders() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = userService.findByUsername(userDetails.getUsername());
//
////        List<OrderItem> userOrders = user.getOrders();
//
//
//    }
}
