package com.store.api;

import com.store.DTO.ProductColorUserDTO;
import com.store.DTO.sellingProductsDTO;
import com.store.configs.CustomConfiguration;
import com.store.dao.ProductColorDAO;
import com.store.dao.ProductDAO;
import com.store.model.Categories;
import com.store.model.Product_Colors;
import com.store.model.Product_Images;
import com.store.model.Products;
import com.store.service.CatelogyService;
import com.store.service.ProductImgService;
import com.store.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductApi {
	
	@Autowired
	ProductColorDAO dao;
	@Autowired
	ProductService productService;
	@Autowired
	ProductImgService productImgService;

    @Autowired
    private CatelogyService catelogyService;
	@Autowired
	CustomConfiguration customConfiguration;
	
	
	

	@RequestMapping("/api/product-color/{colorId}")
	public ResponseEntity<List<ProductColorUserDTO>> postColor(@PathVariable Optional<Long> colorId) {
		Optional<Product_Colors> productColors = dao.findById(colorId.get());
		List<Product_Images> images = productImgService.findByProductcolorId(productColors.get());
		List<ProductColorUserDTO> ProductGetImg = new ArrayList<>();
		for (Product_Images image : images) {
		ProductColorUserDTO saveImg =  new ProductColorUserDTO();
			saveImg.setImgId(image.getImgID());
			saveImg.setImage(image.getImage());
			saveImg.setColorId(colorId.get());
			ProductGetImg.add(saveImg);
		}
		//vòng lặp trả về 1 images.get(index).getimage()/
//		images.get(0)
		return new ResponseEntity<List<ProductColorUserDTO>>(ProductGetImg, HttpStatus.OK);
	}

}
