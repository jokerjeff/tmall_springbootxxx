package com.how2java.tmall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Property;

public interface PropertyDao extends JpaRepository<Property, Integer>{
	//注意看，这个是个接口，它是没有实现类的，至少我们是没有显式提供实现类。 那么要进行条件查询，就是在方法名上面做文章。
	//比如这里的findByCategory，就是基于Category进行查询，第二个参数传一个 Pageable ， 就支持分页了。
	Page<Property> findByCategory(Category category, Pageable pageable);
	List<Property> findByCategory(Category category);
}
