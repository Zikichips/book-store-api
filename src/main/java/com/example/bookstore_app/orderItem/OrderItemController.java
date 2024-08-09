package com.example.bookstore_app.orderItem;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderItemController {
    private OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

//    public boolean createOrderItem() {
//        boolean orderItemAdded = orderItemService.createOrderItem(orderItem);
//        if(orderItemAdded) {
//            return true;
//        }
//        return false;
//    }
}
