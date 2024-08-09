package com.example.bookstore_app.orderItem;

import com.example.bookstore_app.cartItem.CartItem;
import com.example.bookstore_app.purchaseOrder.PurchaseOrder;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    private OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
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
}
