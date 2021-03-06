package com.how2java.tmall.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.ProductImageService;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.util.ImageUtil;

@RestController
public class ProductImageController {
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private ProductService productService;
	
	/*问：@RequestParam是否多余呢，因为默认就会从url中匹配参数。答：是的，如果参数名称和parameter名称一样，就可以不用写*/
	@GetMapping("/products/{pid}/productImages")
	public List<ProductImage> list(@RequestParam("type") String type, @PathVariable("pid") int pid) 
			throws Exception{
		Product product = productService.get(pid);
		if(ProductImageService.type_single.equals(type)){
			List<ProductImage> singles = productImageService.listSingleProductImages(product);
			return singles;
		}else if(ProductImageService.type_detail.equals(type)){
			List<ProductImage> details = productImageService.listDetailProductImages(product);
			return details;
		}else{
			return new ArrayList<>();
		}
	}
	
	@PostMapping("/productImages")
	public Object add(@RequestParam("pid") int pid, @RequestParam("type") String type,
			MultipartFile image, HttpServletRequest request){
		//1、设置完整的bean
		ProductImage bean = new ProductImage();
		Product product = productService.get(pid);
		bean.setType(type);
		bean.setProduct(product);
		//2、保存到数据库
		productImageService.add(bean);
		//3、保存图片
		String folder = "img/";
		if(ProductImageService.type_single.equals(type)){
			folder +="productSingle";
		}else{
			folder +="productDetail";
		}
		File imageFolder = new File(request.getServletContext().getRealPath(folder));
		File file = new File(imageFolder, bean.getId() + ".jpg");
		String fileName = file.getName();
		//System.out.println("---------------fileName = " + fileName + "---------------");
		if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
		try {
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);           
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		if(ProductImageService.type_single.equals(bean.getType())){
			String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
            f_small.getParentFile().mkdirs();
            f_middle.getParentFile().mkdirs();
            ImageUtil.resizeImage(file, 56, 56, f_small);
            ImageUtil.resizeImage(file, 217, 190, f_middle);
		}
		
		return bean;
	}
	
	@DeleteMapping("/productImages/{id}")
	public String delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception{
		//1、删除数据库数据
		ProductImage bean = productImageService.get(id);
		productImageService.delete(id);
		//2、删除本地项目图片
		String folder = "img/";
		if(ProductImageService.type_single.equals(bean.getType())){
			folder +="productSingle";
		}else{
			folder +="productDetail";
		}
		
		File imageFolder = new File(request.getServletContext().getRealPath(folder));
		File file = new File(imageFolder, bean.getId()+".jpg");
		String fileName = file.getName();
		file.delete();
		if(ProductImageService.type_single.equals(bean.getType())){
			String imageFolder_small = request.getServletContext().getRealPath("img/productSingle_small");
			String imageFolder_middle = request.getServletContext().getRealPath("img/productSingle_middle");
			File f_small = new File(imageFolder_small, fileName);
			File f_middle = new File(imageFolder_middle, fileName);
			f_small.delete();
			f_middle.delete();
		}
		
		return null;
	}
}


