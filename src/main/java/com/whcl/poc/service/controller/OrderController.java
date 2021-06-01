package com.whcl.poc.service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.whcl.poc.service.OrderService;
import com.whcl.poc.service.model.Order;

@RestController
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/api/v1.0/order/create")
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
		return ResponseEntity.ok(orderService.createOrder(order));
	}
	
	@GetMapping("/api/v1.0/order/all")
	public List<Order> findAllOrder() {
		return orderService.findAllOrder();
	}
	
	@PutMapping("/api/v1.0/order/cancel")
	private ResponseEntity<String> cancelOrder(@RequestBody Order order) {
		return ResponseEntity.ok(orderService.cancelOrder(order));
	}
	
	@GetMapping("/api/v1.0/orders/{orderId}")
	private Order findOrderByOrderId(@PathVariable long orderId) {
		return orderService.findOrderByOrderId(orderId);
	}
	
	@GetMapping("/api/v1.0/order/restaurant/{restaurantId}")
	private ResponseEntity<List<Order>> findOrderByRestaurantId(@PathVariable long restaurantId) {
		return ResponseEntity.ok(orderService.findOrderByRestaurantId(restaurantId));

	}
	
	@GetMapping("/api/v1.0/order/driver/{driverid}")
	public ResponseEntity<List<Order>> getOrderByDriverId(@PathVariable long driverid) {
		return ResponseEntity.ok(orderService.findOrderByDriverId(driverid));
	}
	
	@PutMapping("/api/v1.0/order/assign/driver")
	public ResponseEntity<String> assignOrderToDriver(@RequestBody Map<String, Integer> map) {

		return ResponseEntity.ok(orderService.assignDriverToOrder(map));
	}
	
}
