package com.store.dao;

import com.store.model.Categories;
import com.store.model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
@Repository
public interface ProductDAO extends JpaRepository<Products, String> {

    List<Products> findByDeprecated(Boolean deprecated);
    Page<Products> findByDeprecated(Boolean deprecated, Pageable pageable);
    void deleteProductsByProductID(String ProductID);
    @Modifying(clearAutomatically = true)
    @Query(value = "update Products set deprecated = 1 WHERE productID = ?", nativeQuery = true)
    void updateLocal(String productID);
    List<Products> findByCategoryIn(Collection<Categories> categoryIds);

    @Modifying(clearAutomatically = true)
    @Query(value = "update Products set deprecated = 0 WHERE productID = ?", nativeQuery = true)
    void updateStatusTrue(String productID);
    Products findByProductID(String name);
    //search full
    @Query(nativeQuery = true, value = "select * from [dbo].[searchFullTextProducts](:search1)")
    List<Products> findSearch(@Param("search1") String search);
    @Query(nativeQuery = true, value = "select * from [dbo].[full_text_categoryId]( :categoryId, :search12)")
    List<Products> findSearchAndCateId1(@Param("search12") String search, @Param("categoryId") String cate);
    @Query(nativeQuery = true, value = "select * from [dbo].[full_text_statusF](:search1)")
    List<Products> findSearchAndSatusF(@Param("search1") String search);
    @Query(nativeQuery = true, value = "select * from [dbo].[full_text_statusT](:search1)")
    List<Products> findSearchAndSatusT(@Param("search1") String search);
    @Query(nativeQuery = true, value = "select * from [dbo].[searchProductsclient](:search1)")
    List<String> findSearchClient(@Param("search1") String search);
    //Home
    @Query(nativeQuery = true, value = "Select top (20) productID, SUM(quantity) bestSale FROM order_details Group By productID order by bestSale desc")
    List<String> sellingproducts();
    @Query(nativeQuery = true, value = "select * from SellingProducts(:page)")
    List<String>
    selling(@Param("page") int page);
    @Query(nativeQuery = true, value = "select top(20)* from products where deprecated = 0 order by createDate desc")
    List<Products> ProductsNew();
    // Product User
    @Query(value = "SELECT * from products where categoryID like '%nam' and deprecated = 0 ", nativeQuery = true)

    Page<Products> findMen(Pageable pageable);

    @Query(value = "SELECT * from products where categoryID like '%nu' and deprecated = 0  ", nativeQuery = true)
    Page<Products> findWoman(Pageable pageable);


    @Query(value = "SELECT * from products where categoryID = :categoryId and deprecated = 0 ",nativeQuery = true)
    Page<Products> finByCategoryId(String categoryId, Pageable pageable);

    @Query(value = "SELECT * from products  where [name] LIKE %?1% and deprecated = 0", nativeQuery = true)
    Page<Products> findAll(String keyword, Pageable pageable);

    @Query(value = "SELECT * from products where deprecated = 0  ", nativeQuery = true)
    Page<Products> findByDeprecated(Pageable pageable);

    
    @Query(value = "select * from Fn_FilterPricesDescending(:categoryID,:keyword)", nativeQuery = true)
    Page<Products> findbyPriceMax(String categoryID,String keyword,Pageable pageable);
    
    @Query(value = "select * from Fn_FilterPricesAscending(:categoryID,:keyword)", nativeQuery = true)
    Page<Products> findbyPriceMin(String categoryID,String keyword,Pageable pageable);

    @Query(value = "select * from Fn_TongSP(:month,:year)", nativeQuery = true )
    List<String> getProductTotal(@Param("month")String month, @Param("year") String year);

    @Query(value = "select Top 6 productID   from order_details group by productID \n" +
            "order by sum(quantity) desc ", nativeQuery = true )
    List<String> getProductTop();

    @Query(value = "SELECT Top 6 * from products where categoryID = :categoryId and deprecated = 0  ",nativeQuery = true)
    List<Products> GetCategoryId(String categoryId);
}
