package com.how2java.tmall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.how2java.tmall.pojo.Order;

public interface OrderDAO extends JpaRepository<Order, Integer>{

}
