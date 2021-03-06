package com.how2java.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.how2java.tmall.dao.CategoryDAO;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.util.Page4Navigator;

@Service
public class CategoryService {
	@Autowired CategoryDAO categoryDAO;
	//后台
	public Page4Navigator<Category> list(int start, int size, int navigatePages){
		//start:起始页
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		Pageable pageable = new PageRequest(start, size, sort);
		Page pageFromJPA = categoryDAO.findAll(pageable);
		
		return new Page4Navigator<>(pageFromJPA, navigatePages);
	}
	
	public List<Category> list() {
//		System.out.println("进入CategoryService");
    	Sort sort = new Sort(Sort.Direction.DESC, "id");
		return categoryDAO.findAll(sort);
	}
	
	public void add(Category bean){
		System.out.println("information of  bean = " + bean.getId() + "-" + bean.getName());//此时id均为0
		categoryDAO.save(bean);
//		System.out.println("Category bean = " + bean);
	}
	
	public void delete(int id){
		categoryDAO.delete(id);
	}
	
	public Category get(int id){
		Category c = categoryDAO.findOne(id);
		return c;
	}
	
	public void update(Category bean){
		categoryDAO.save(bean);
	}
	
	//----------------前台？------------------//
	public void removeCategoryFromProduct(List<Category> cs){
		for(Category category : cs){
			removeCategoryFromProduct(category);
		}
	}
	
	public void removeCategoryFromProduct(Category category){
		List<Product> products = category.getProducts();
		if(null!=products) {
            for (Product product : products) {
                product.setCategory(null);
            }
        }
		
		List<List<Product>> productsByRow = category.getProductsByRow();
		if(null!=productsByRow) {
            for (List<Product> ps : productsByRow) {
                for (Product p: ps) {
                    p.setCategory(null);
                }
            }
        }
	}
}
