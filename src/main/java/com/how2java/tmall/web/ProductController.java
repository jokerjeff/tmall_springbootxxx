package com.how2java.tmall.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.util.Page4Navigator;

@RestController
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@PostMapping("/products")
	public Object add(@RequestBody Product bean) throws Exception{
		productService.add(bean);
		return bean;
	}
	
	@DeleteMapping("/products/{id}")
	public String delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception{
		productService.delete(id);
		return null;
	}
	
	@PutMapping("/products")
	public Object update(@RequestBody Product bean) throws Exception{
		productService.update(bean);
		return bean;
	}
	
	@GetMapping("/products/{id}")
	public Product get(@PathVariable("id") int id) throws Exception{
		Product bean = productService.get(id);
		return bean;
	}
	
	@GetMapping("/categories/{cid}/products")
	public Page4Navigator<Product> list(@PathVariable("cid") int cid, 
			@RequestParam(name="start", defaultValue="0") int start,
			@RequestParam(name="size", defaultValue="5") int size) throws Exception{
		start = start < 0 ? 0 : start;		
		Page4Navigator<Product> page =productService.list(cid, start, size,5);
		return page;
	}
}