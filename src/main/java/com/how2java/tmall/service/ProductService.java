package com.how2java.tmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.how2java.tmall.dao.ProductDAO;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.util.Page4Navigator;

@Service
public class ProductService {
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private CategoryService categoryService;
	
	public void add(Product bean){
		productDAO.save(bean);
	}
	
	public void delete(int id){
		productDAO.delete(id);
	}
	
	public void update(Product bean){
		productDAO.save(bean);
	}
	
	public Product get(int id){
		return productDAO.findOne(id);
	}
	
	public Page4Navigator<Product> list(int cid, int start, int size, int navigatePages){
		Category category = categoryService.get(cid);
		
		Sort sort = new Sort(Sort.Direction.DESC, "id");//排序，倒序分页工具
		Pageable pageable = new PageRequest(start, size, sort);
		
		Page<Product> pageFromJPA = productDAO.findByCategory(category, pageable);
		
		return new Page4Navigator<>(pageFromJPA, navigatePages);
	}
}
