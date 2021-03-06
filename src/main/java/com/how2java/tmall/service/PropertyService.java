package com.how2java.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.how2java.tmall.dao.PropertyDao;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.util.Page4Navigator;

@Service
public class PropertyService {
	@Autowired
	private PropertyDao propertyDao;
	@Autowired
	private CategoryService categoryService;
	
	public void add(Property bean){
		propertyDao.save(bean);
	}
	
	public void delete(int id){
		propertyDao.delete(id);
	}
	
	public void update(Property bean){
		propertyDao.save(bean);
	}
	
	public Property get(int id){
		return propertyDao.findOne(id);
	}
	
	//因为在业务上需要查询某个分类下的属性，所以list方法会带上对应分类的id。  start:起始页;size:每页显示的最多个数
	//后台
	public Page4Navigator<Property> list(int cid, int start, int size, int navigatePages){
		Category category = categoryService.get(cid);
		
		Sort sort = new Sort(Sort.Direction.DESC, "id");//排序，倒序分页工具
        Pageable pageable = new PageRequest(start, size, sort);// 分页工具
        
        Page<Property> pageFromJPA =propertyDao.findByCategory(category,pageable);//分页工具
        
		return new Page4Navigator<>(pageFromJPA,navigatePages);
	}
	
	public List<Property> listByCategory(Category category){
        return propertyDao.findByCategory(category);
    }
}
