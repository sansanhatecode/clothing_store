    package com.store.api;
    import com.store.DTO.ProductColorDTO;
    import com.store.DTO.ProductColorUserDTO;
    import com.store.configs.CustomConfiguration;
    import com.store.dao.ProductDAO;
    import com.store.model.Colors;
    import com.store.model.Product_Colors;
    import com.store.model.Product_Images;
    import com.store.model.Products;
    import com.store.service.ProductColorsService;
    import com.store.service.ProductService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @RestController
    public class ProductColorApi {
        @Autowired
        ProductColorsService productColorsService;
        @Autowired
        ProductService prd;
        @Autowired
        ProductService productService;
        @Autowired
        CustomConfiguration customConfiguration;
        @RequestMapping("/admin/product/listProductColorByProductId/{id}")
        public List<Product_Colors>  listProductColorByProductId(@PathVariable String id) {
            List<Product_Colors> prdColor = productColorsService.findbyProductID(id);
            return prdColor ;
        }
        @RequestMapping("/admin/product/lt")
        public List<Products>  lt() {
            List<Products> prdColor = prd.findAll();
            return prdColor ;
        }
        @RequestMapping("/product/search/posts")
        public List<String> prd(@RequestBody String id) {
            List<String> prd = productService.findSearchClient(id);
            return prd ;
        }
//        /admin/product/listProductColorByProductId
    }
