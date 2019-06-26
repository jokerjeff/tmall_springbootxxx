package com.how2java.tmall.web;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.Page4Navigator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
 
@RestController
public class CategoryController {
	@Autowired CategoryService categoryService;
	
	@GetMapping("/categories")//查询
    public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0") int start,
    		@RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
//		System.out.println("进入了CategoryController");
		start = start < 0 ? 0 : start;
		//5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
		Page4Navigator<Category> page = categoryService.list(start, size, 5);
    	return page;
    }
	
	@PostMapping("/categories")//增加
	public Object add(Category bean, MultipartFile image, HttpServletRequest request) throws IOException{
		categoryService.add(bean);
		saveOrUpdateImageFile(bean, image, request);
//		System.out.println("bean in controller return : " + bean);
		return bean;
	}
	
	public void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request) 
			throws IOException{
		File imageFolder= new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,bean.getId()+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);

	}
	
	@DeleteMapping("/categories/{id}")//删除
	public String delete(@PathVariable("id") int id, HttpServletRequest request){
		categoryService.delete(id);
		File imageFolder = new File(request.getServletContext().getRealPath("image/category"));
		File file = new File(imageFolder, id + ".jpg");
		file.delete();
		return null;
	}
	
	@GetMapping("/categories/{id}")//查询
	public Category get(@PathVariable("id") int id){
		Category bean = categoryService.get(id);
		return bean;
	}
	
	@PutMapping("/categories/{id}")//修改
    public Object update(Category bean, MultipartFile image,HttpServletRequest request) throws Exception {
        //这里获取参数用的是 request.getParameter("name"). 为什么不用 add里的注入一个 Category对象呢？ 
		//因为。。。PUT 方式注入不了。。。 只能用这种方式取参数了
		String name = request.getParameter("name");
        bean.setName(name);
        categoryService.update(bean);
 
        if(image!=null) {
            saveOrUpdateImageFile(bean, image, request);
        }
        System.out.println("增加一个输出语句");
        return bean;
    }
}

