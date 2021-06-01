package com.whcl.poc.service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.whcl.poc.service.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>{

	Order findByOrderId(long orderId);

	List<Order> findByRestaurantId(long restaurantId);

	List<Order> findByDriverId(long driverid);
	
}
