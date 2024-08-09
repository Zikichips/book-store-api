package com.example.bookstore_app.purchaseOrder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriber")
public class PurchaseOrderController {

    private PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping("/order")
    public ResponseEntity<String> createOrder(@RequestBody PurchaseOrderProcessingDTO orderShippingAddressDTO) {
        boolean orderCreated = purchaseOrderService.createOrder(orderShippingAddressDTO);

        if(orderCreated) {
            return new ResponseEntity<>("Order created successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Order could not be created", HttpStatus.BAD_REQUEST);

    }


}
