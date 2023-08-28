package com.store.service;



import com.store.model.Products;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.store.DTO.sellingProductsDTO;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    public void deleteLogical(String productID);

    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    public void deleteProduct(String productID);

    Products save(Products product) throws SQLException;

    List<Products> findDeprecatedTrue();
    List<String> findSearchClient(String name);
    List<Products> findDeprecatedFalse();
    List<Products> findBySearch(String search);
    List<Products> findBySearchDb(String search);
    List<Products> findBySearchNb(String search);
    List<Products> findByseatchCateId(String search, String cate);
    Optional<Products> findByID(String productID);
//    List<Products> findByCategory(String cate);
    Products findProducts(String name);
    Page<Products> findAll(int pageSize, int pageNumber) throws Exception;
    public  void updateStatusTrue(String productID);
    Page<Products> findPaginated(Pageable pageable, List sql);
    List<String> findsellingProducts();
    List<Products> productsNew();
    // ProductUser

    List<Products> findAll();
    String callHelloWorld();
    Page<Products> findMen(int pageSize, int pageNumber) throws  Exception;

    Page<Products> findWomen(int pageSize, int pageNumber) throws  Exception;

    Page<Products> findByAll(int pageSize, int pageNumber) throws  Exception;

    Products findByProductID(String productID);

    Products findById(String productID);

    Page<Products> findAllByCategoryId(String categoryId,int pageSize, int pageNumber) throws  Exception;

    Page<Products> listAll(String keyword,int pageSize, int pageNumber) throws  Exception;

    Page<Products> findbyPriceMax(String categoryID, String keyword, int pageSize, int pageNumber) throws  Exception;
    Page<Products> findbyPriceMin(String categoryID, String keyword,int pageSize, int pageNumber) throws  Exception;

    List<String> getProductTop();

    List<Products> GetCategoryId(String categoryId);
}
