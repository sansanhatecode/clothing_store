package com.store.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.model.Categories;
import com.store.model.Order_Details;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
public class ProductDTO {
    String productID;
    String name;
    MultipartFile img;
    Double price;
    @Column(name = "Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date createDate = new Date();
    boolean deprecated;
    long colorId;
    String description;
    String type;
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryID")
    Categories category;
    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    List<Order_Details> orderDetails;
    String search;
    public ProductDTO() {
    }
    public List<String> listProductByCategory = new ArrayList<>();
    public static List<String> listCate = new ArrayList<>();
    public ProductDTO(String productID, String name, MultipartFile img, Double price, Date createDate, boolean deprecated, String description, Categories category, List<Order_Details> orderDetails) {
        this.productID = productID;
        this.name = name;
        this.img = img;
        this.price = price;
        this.createDate = createDate;
        this.deprecated = deprecated;
        this.description = description;
        this.category = category;
        this.orderDetails = orderDetails;
    }
}
