package com.how2java.tmall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;

public interface OrderItemDAO extends JpaRepository<OrderItem, Integer>{
	List<OrderItem> findByOrderOrderByIdDesc(Order order);
}
