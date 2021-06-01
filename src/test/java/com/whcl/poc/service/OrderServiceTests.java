package com.whcl.poc.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.whcl.poc.service.model.Order;
import com.whcl.poc.service.model.OrderItem;
import com.whcl.poc.service.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
class OrderServiceTests {

	@Test
	void contextLoads() {
	}
	
	@Mock
	private OrderRepository orderRepo;
	
	@InjectMocks
	private OrderService orderService;
	
	@Test
	public void createOrderTest() {
		
		Order order = new Order();
		order.setCustomerId(2);
		order.setRestaurantId(3);
		
		List<OrderItem> list = new ArrayList<OrderItem>();
		
		OrderItem item1= new OrderItem();
		item1.setItem_id(1);
		item1.setName("Biriyani");
		item1.setQuantity(2);
		item1.setUnitPrice(150);
		item1.setAverageFoodPreaparationTime(40);
		
		OrderItem item2= new OrderItem();
		item2.setItem_id(3);
		item2.setName("Paratha");
		item2.setQuantity(5);
		item2.setUnitPrice(100);
		item2.setAverageFoodPreaparationTime(40);
		
		list.add(item1);
		list.add(item2);
		order.setList(list);
		
		when(orderRepo.save(order)).thenReturn(order);
		
		assertEquals(orderService.createOrder(order).getOrderStatus(), "ORDER CREATED");
		
	}
	
	@Test
	public void createOrderTest_FAILED() {
		
		Order order = new Order();
		order.setCustomerId(2);
		order.setRestaurantId(3);
		
		List<OrderItem> list = new ArrayList<OrderItem>();
		
		OrderItem item1= new OrderItem();
		item1.setItem_id(1);
		item1.setName("Biriyani");
		item1.setQuantity(2);
		item1.setUnitPrice(150);
		item1.setAverageFoodPreaparationTime(40);
		
		OrderItem item2= new OrderItem();
		item2.setItem_id(3);
		item2.setName("Paratha");
		item2.setQuantity(5);
		item2.setUnitPrice(100);
		item2.setAverageFoodPreaparationTime(40);
		
		list.add(item1);
		list.add(item2);
		order.setList(list);
		
		when(orderRepo.save(order)).thenReturn(order);
		
		assertNotEquals(orderService.createOrder(order).getOrderStatus(), "ORDER");
		
	}
	
	@Test
	void findOrderByRestaurantIdTest(){
		
		List<Order> orderList = new ArrayList<Order>();
		
		Order order = new Order();
		order.setCustomerId(2);
		order.setRestaurantId(3);
		
		List<OrderItem> list = new ArrayList<OrderItem>();
		
		OrderItem item1= new OrderItem();
		item1.setItem_id(1);
		item1.setName("Biriyani");
		item1.setQuantity(2);
		item1.setUnitPrice(150);
		item1.setAverageFoodPreaparationTime(40);
		
		OrderItem item2= new OrderItem();
		item2.setItem_id(3);
		item2.setName("Paratha");
		item2.setQuantity(5);
		item2.setUnitPrice(100);
		item2.setAverageFoodPreaparationTime(40);
		
		list.add(item1);
		list.add(item2);
		order.setList(list);
		orderList.add(order);
		
		when(orderRepo.findByRestaurantId(3)).thenReturn(orderList);
		
		assertEquals(orderService.findOrderByRestaurantId(order.getRestaurantId()).size(), 1);

	}

}
