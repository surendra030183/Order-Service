package com.whcl.poc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.whcl.poc.service.model.Order;
import com.whcl.poc.service.model.OrderItem;
import com.whcl.poc.service.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

	//TODO, these constants will moved to as enum or will be used from table
	private static final String ORDERID = "orderid";
	private static final String DRIVERID = "driverid";
	private static final String CANCELLED = "CANCELLED";
	private static final String FAILED_TO_CANCEL_ORDER = "FAILED TO CANCEL ORDER";
	private static final String DRIVER_ASSIGNED_TO_AN_ORDER_SUCCESSFULLY = "Driver assigned to an order successfully";
	private static final String YOUR_ORDER_IS_CANCELLED = "Your order is cancelled.";
	private static final String ASSIGNED_TO_DRIVER_FOR_DELIVERY = "ASSIGNED TO DRIVER FOR DELIVERY";
	private static final String ORDER_CREATED = "ORDER CREATED";
	protected final Log logger = LogFactory.getLog(OrderService.class);
	
	@Value("${tax}")
	private double taxrate;
	
	@Value("${delivery.charge}")
	private double deliveryCharge;
	
	@Value("${average.delivery.time}")
	private int averageDeliveryTime;
	
	@Autowired
	private OrderRepository orderRepository;

	/**
	 * Create new order
	 * @param order
	 * @return
	 */
	public Order createOrder(Order order) {
		logger.info("Create order request send by customer:"+order.getCustomerId()+", Restaurant:"+order.getRestaurantId());

		order.setOrderDate(new Date());
		order.setOrderStatus(ORDER_CREATED);
		order.setTotalPrice(calculatePrice(order));
		order.setEstimatedTotalTime(getTotalEstimationTime(order));
		order.setOrderId(generateUniqueOrder());
		return orderRepository.save(order);
	}

	/**
	 * Get total estimation time to deliver order to the customer
	 * @param order
	 * @return
	 */
	private int getTotalEstimationTime(Order order) {
		
		int totalTime = getDeliveryTime(order) + estimatedFoodPreparationTime(order);
		
		return totalTime;
	}

	/**
	 * Get delivery time once order is prepared
	 * @param order
	 * @return
	 */
	private int getDeliveryTime(Order order) {
		
		int time = (int) ((order.getDistance()/ averageDeliveryTime )*60);
		return time;
	}

	/**
	 * Generate unique number for order
	 * @return
	 */
	private int generateUniqueOrder() {
		//TODO, this logic should be changed in future
		Random rnd = new Random();
	    int number = rnd.nextInt(999999);
		return number;
	}

	/**
	 * Get estimated time for food preparation 
	 * @param order
	 * @return
	 */
	private int estimatedFoodPreparationTime(Order order) {
		List<Integer> collect = order.getList().stream().map(e -> e.getAverageFoodPreaparationTime()*e.getQuantity()).collect(Collectors.toList());
		Integer estimatedPrepTime = collect.stream().max(Integer::compare).get();
		
		return estimatedPrepTime;
	}

	/**
	 * Calculate total price for order
	 * @param order
	 * @return
	 */
	private double calculatePrice(Order order) {
		double totalPrice = 0;
		List<OrderItem> list = order.getList();
		
		for (OrderItem item : list) {
			
			double baseCost = item.getUnitPrice() * item.getQuantity();
			double tax = (baseCost * taxrate)/100;
			
			totalPrice+= baseCost + tax;
		}
		
		totalPrice += (deliveryCharge * order.getDistance());
		
		return totalPrice;
	}

	/**
	 * Find all order
	 * @return
	 */
	public List<Order> findAllOrder() {
		return orderRepository.findAll();
	}

	/**
	 * Cancel an order 
	 * @param order
	 * @return
	 */
	public String cancelOrder(Order order) {
		Order orderDb = orderRepository.findByOrderId(order.getOrderId());
		if(orderDb != null && orderDb.getCustomerId()==order.getCustomerId()) {
			orderDb.setOrderStatus(CANCELLED);
			orderRepository.save(orderDb);
			logger.info("order:"+order.getOrderId()+ "is cancelled by customer:"+order.getCustomerId());
			
			return YOUR_ORDER_IS_CANCELLED;
		}
		
		return FAILED_TO_CANCEL_ORDER;
	}

	/**
	 * Find order by order id
	 * @param orderId
	 * @return
	 */
	public Order findOrderByOrderId(long orderId) {
		return orderRepository.findByOrderId(orderId);
	}

	/**
	 * Find order by restaurant id
	 * @param restaurantId
	 * @return
	 */
	public List<Order> findOrderByRestaurantId(long restaurantId) {
		return orderRepository.findByRestaurantId(restaurantId);
	}

	/**
	 * Find order by driver id, order which is assigned to a driver
	 * @param driverid
	 * @return
	 */
	public List<Order> findOrderByDriverId(long driverid) {
		return orderRepository.findByDriverId(driverid);
	}

	/**
	 * Assign order to driver 
	 * @param map
	 * @return
	 */
	public String assignDriverToOrder(Map<String, Integer> map) {
		Integer orderid = map.get(ORDERID);
		Integer driverid = map.get(DRIVERID);
		
		logger.info("orderid:"+orderid+", assigning driver:"+driverid+" for delivery");
		if(driverid!=null && orderid !=null) {
			Order order=findOrderByOrderId(orderid);
			if(order != null) {
				order.setDriverId(driverid);
				order.setOrderStatus(ASSIGNED_TO_DRIVER_FOR_DELIVERY);
				orderRepository.save(order);
			}
		}
		return DRIVER_ASSIGNED_TO_AN_ORDER_SUCCESSFULLY;
	}

	
}
