package com.example.bookstore_app.purchaseOrder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriber")
public class PurchaseOrderController {

    private PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<PurchaseOrder>> getAllOrders() {
            List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllUserOrders();
            return new ResponseEntity<>(purchaseOrders, HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<PurchaseOrder> getOrderById(@PathVariable Long id) {
        PurchaseOrder order = purchaseOrderService.getOrderById(id);
        if(order.getId() != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
