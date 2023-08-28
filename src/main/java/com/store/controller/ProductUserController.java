package com.store.controller;

import com.store.DTO.sellingProductsDTO;
import com.store.configs.CustomConfiguration;
import com.store.model.Categories;
import com.store.model.Product_Colors;
import com.store.model.Products;
import com.store.service.CatelogyService;
import com.store.service.ProductColorsService;
import com.store.service.ProductImgService;
import com.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductUserController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CatelogyService catelogyService;
    @Autowired
    private ProductColorsService productColorsService;
    @Autowired
    CustomConfiguration customConfiguration;
    @Autowired
    ProductImgService productImgService;
    private static final int MAX_SIZE = 6;
    private static final int MAX_SIZEFull = 100;

    String ProductCate = "%";
    String ProductKeyword = "%";
    @RequestMapping({"/productgird"})
    public String productGird(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
        List<Products> products = new ArrayList<>();
        List<sellingProductsDTO> prdM = new ArrayList<>();
        try {
            Page<Products> pageProduct = productService.findByAll(MAX_SIZE, page);
            products = pageProduct.getContent();
            products.forEach(item -> {
                sellingProductsDTO prd = customConfiguration.modelMapper().map(item, sellingProductsDTO.class);
                prd.setColor(productColorsService.findbyProductID(item.getProductID()));
                List<Product_Colors> color = productColorsService.findbyProductID(item.getProductID());
                prd.setNameImg(productImgService.top3NameImg(color.get(0).getColorID()));
                prdM.add(prd);
            });
            model.addAttribute("totalPages", pageProduct.getTotalPages());
            model.addAttribute("currentPage", page);
        } catch (Exception e) {
            products = productService.findAll();
        }

        model.addAttribute("products", prdM);
        List<Categories> categories = catelogyService.findAll();
        model.addAttribute("categories", categories);
        ProductCate = "%";
        ProductKeyword = "%";
        return "/layout/productgird";
    }
    @RequestMapping({"/productSearch"})
    public String productSearch(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                Model model, @Param("keyword") String keyword
                                ) {
        List<Products> products = new ArrayList<>();
        List<sellingProductsDTO> prdM = new ArrayList<>();
        try {
            Page<Products> pageProduct = productService.listAll(keyword,MAX_SIZE, page);
            products = pageProduct.getContent();
            products.forEach(item -> {
                sellingProductsDTO prd = customConfiguration.modelMapper().map(item, sellingProductsDTO.class);
                prd.setColor(productColorsService.findbyProductID(item.getProductID()));
                List<Product_Colors> color = productColorsService.findbyProductID(item.getProductID());
                prd.setNameImg(productImgService.top3NameImg(color.get(0).getColorID()));
                prdM.add(prd);
            });
            model.addAttribute("totalPages", pageProduct.getTotalPages());
            model.addAttribute("currentPage", page);
            model.addAttribute("url","/productSearch?keyword=" + keyword);
        } catch (Exception e) {
            products = productService.findAll();
        }
        model.addAttribute("products", prdM);
        List<Categories> categories = catelogyService.findAll();
        model.addAttribute("categories", categories);
        ProductCate = "%";
        ProductKeyword = "%" +keyword + "%";
        return "/layout/productSearch";
    }

    @RequestMapping("/product/{productID}")
    public String productID(@PathVariable("productID") String productID,
                            Model model) {
        List<Product_Colors> product_colors = productColorsService.findbyProductID(productID);
        Products product = productService.findById(productID);
        if (product_colors == null) {
            return "redirect:/home";
        } else {
            model.addAttribute("product", product);
            model.addAttribute("product_color", product_colors);
            model.addAttribute("productID", productID);
        }
        // Top sản phẩm
        List<String> productTop = productService.getProductTop();
        List<Products> productname = new ArrayList<>();
        for(int i = 0 ; i < 5; i ++){
            Products pN = productService.findByProductID(productTop.get(i));
            productname.add(pN);
        }
        model.addAttribute("productTop", productname);

        // Sản phẩm cùng loại
        String CateID = productService.findById(productID).getCategory().getCategoryID();
        List<Products> productCate = productService.GetCategoryId(CateID);
        List<sellingProductsDTO> prdM = new ArrayList<>();
        productCate.forEach(item -> {
            sellingProductsDTO prd = customConfiguration.modelMapper().map(item, sellingProductsDTO.class);
            prd.setColor(productColorsService.findbyProductID(item.getProductID()));
            List<Product_Colors> color = productColorsService.findbyProductID(item.getProductID());
            prd.setNameImg(productImgService.top3NameImg(color.get(0).getColorID()));
            prdM.add(prd);
        });
        model.addAttribute("products", prdM);
        return "layout/productDetails";
    }

    @RequestMapping("/productGird/{categoryID}")
    public String productGirdCategory(@PathVariable("categoryID") String categoryID,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
        Categories category = catelogyService.findByCategoryID(categoryID);
        if (category == null) {
            return "redirect:/home";
        } else {
            List<Products> products = new ArrayList<>();
            List<sellingProductsDTO> prdM = new ArrayList<>();
            try {
                Page<Products> pageProduct = productService.findAllByCategoryId(categoryID,MAX_SIZE, page);
                products = pageProduct.getContent();
                products.forEach(item -> {
                    sellingProductsDTO prd = customConfiguration.modelMapper().map(item, sellingProductsDTO.class);
                    prd.setColor(productColorsService.findbyProductID(item.getProductID()));
                    List<Product_Colors> color = productColorsService.findbyProductID(item.getProductID());
                    prd.setNameImg(productImgService.top3NameImg(color.get(0).getColorID()));
                    prdM.add(prd);
                });
                model.addAttribute("totalPages", pageProduct.getTotalPages());
                model.addAttribute("currentPage", page);
            } catch (Exception e) {
                products = productService.findAll();
            }
            model.addAttribute("products", prdM);
        }


        List<Categories> categories = catelogyService.findAll();
        model.addAttribute("categories", categories);
        ProductCate = categoryID;
        ProductKeyword = "%";
        return "layout/productgird";
    }

    @RequestMapping("/productList/{categoryID}")
    public String productListCategory(@PathVariable("categoryID") String categoryID,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
        Categories category = catelogyService.findByCategoryID(categoryID);
        if (category == null) {
            return "redirect:/home";
        } else {
            List<Products> products = new ArrayList<>();
            try {
                Page<Products> pageProduct = productService.findAllByCategoryId(categoryID,MAX_SIZE, page);
                products = pageProduct.getContent();
                model.addAttribute("totalPages", pageProduct.getTotalPages());
                model.addAttribute("currentPage", page);
            } catch (Exception e) {
                products = productService.findAll();
            }
            model.addAttribute("products", products);
        }


        List<Categories> categories = catelogyService.findAll();
        model.addAttribute("categories", categories);
        return "layout/productlist";
    }

    @RequestMapping("/productlist")
    public String prodctList(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
        List<Products> products = new ArrayList<>();
        try {
            Page<Products> pageProduct = productService.findAll(MAX_SIZE, page);
            products = pageProduct.getContent();
            model.addAttribute("totalPages", pageProduct.getTotalPages());
            model.addAttribute("currentPage", page);
        } catch (Exception e) {
            products = productService.findAll();
        }

        model.addAttribute("products", products);
        List<Categories> categories = catelogyService.findAll();
        model.addAttribute("categories", categories);
        return "/layout/productlist";
    }
    @RequestMapping("/productPriceMax")
    public String productpriceMax(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
        List<Products> products = new ArrayList<>();
        List<sellingProductsDTO> prdM = new ArrayList<>();
        try {
            Page<Products> pageProduct = productService.findbyPriceMax(ProductCate, ProductKeyword,MAX_SIZE, page);
            products = pageProduct.getContent();
            products.forEach(item -> {
                sellingProductsDTO prd = customConfiguration.modelMapper().map(item, sellingProductsDTO.class);
                prd.setColor(productColorsService.findbyProductID(item.getProductID()));
                List<Product_Colors> color = productColorsService.findbyProductID(item.getProductID());
                prd.setNameImg(productImgService.top3NameImg(color.get(0).getColorID()));
                prdM.add(prd);
            });
            model.addAttribute("totalPages", pageProduct.getTotalPages());
            model.addAttribute("currentPage", page);
        } catch (Exception e) {
            products = productService.findAll();
        }

        model.addAttribute("products", prdM);
        List<Categories> categories = catelogyService.findAll();
        model.addAttribute("categories", categories);
        return "/layout/productgird";
    }
    @RequestMapping("/productPriceMin")
    public String productPriceMin(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
        List<Products> products = new ArrayList<>();
        List<sellingProductsDTO> prdM = new ArrayList<>();
        try {
            Page<Products> pageProduct = productService.findbyPriceMin(ProductCate, ProductKeyword,MAX_SIZE, page);
            products = pageProduct.getContent();
            products.forEach(item -> {
                sellingProductsDTO prd = customConfiguration.modelMapper().map(item, sellingProductsDTO.class);
                prd.setColor(productColorsService.findbyProductID(item.getProductID()));
                List<Product_Colors> color = productColorsService.findbyProductID(item.getProductID());
                prd.setNameImg(productImgService.top3NameImg(color.get(0).getColorID()));
                prdM.add(prd);
            });
            model.addAttribute("totalPages", pageProduct.getTotalPages());
            model.addAttribute("currentPage", page);
        } catch (Exception e) {
            products = productService.findAll();
        }

        model.addAttribute("products", prdM);
        List<Categories> categories = catelogyService.findAll();
        model.addAttribute("categories", categories);
        return "/layout/productgird";
    }
    
}
