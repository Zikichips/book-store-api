package com.example.bookstore_app.orderItem;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderItemController {
    private OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

//    @GetMapping("/orders")
//    public ResponseEntity<List<OrderItem>> getAllOrders() {
//        List<OrderItem> orderItems = orderItemService.getAllUserOrders();
//    }
}
