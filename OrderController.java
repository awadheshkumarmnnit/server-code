package com.github.parasmani300.rmsorders.controller;

import com.github.parasmani300.rmsorders.dto.ErrorMessage;
import com.github.parasmani300.rmsorders.dto.Updater;
import com.github.parasmani300.rmsorders.model.Order;
import com.github.parasmani300.rmsorders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/makeOrder")
    public ResponseEntity<?> makeOrder(@RequestBody Order order)
    {
        Order order1 = orderService.makeOrder(order);
        if(order1 == null){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setMessage("Item doesnot exist in store or store doesn't exist");
            return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.OK);
        }
        return  new ResponseEntity<Order>(order1, HttpStatus.CREATED);
    }

    @PostMapping("/makeMultiLineOrder")
    public ResponseEntity<?> makeMultiLineOrder(@RequestBody Order[] order)
    {
        List<Order> order1 = orderService.makeOrder(order);
        return  new ResponseEntity<List<Order>>(order1, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(value = "pageNo",defaultValue = "1",required = false) Integer pageNo,
            @RequestParam(value = "pageSize",defaultValue = "1",required = false) Integer pageSize
            )
    {
        List<Order> orderList = orderService.fetchAllOrders(pageNo,pageSize);
        return  new ResponseEntity<List<Order>>(orderList,HttpStatus.OK);
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable  Integer orderId)
    {
        Order order = orderService.fetchOrderDetails(orderId);
        if(order == null){
            ErrorMessage errorMessage =  new ErrorMessage();
            errorMessage.setMessage("Order not found");
            return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.OK);
        }
        return new ResponseEntity<Order>(order,HttpStatus.OK);
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Integer orderId, @RequestBody Updater[] updaters) throws ParseException {
        Order order = null;
        for(int i = 0;i<updaters.length;i++)
        {
            order = orderService.updateOrderDetails(orderId,updaters[i].getKey(),updaters[i].getValue());
        }

        if(order == null){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setMessage("No Order found");
            return new ResponseEntity<ErrorMessage>(errorMessage,HttpStatus.OK);
        }

        return new ResponseEntity<Order>(order,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Integer orderId)
    {
        Order order = orderService.deleteOrderDetails(orderId);
        return new ResponseEntity<Order>(order,HttpStatus.OK);
    }
}
