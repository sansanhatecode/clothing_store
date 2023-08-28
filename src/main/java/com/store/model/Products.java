package com.store.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@SuppressWarnings("serial")
@Getter
@Setter
@Entity
@Table(name = "products")
public class Products {
    @Id
    String productID;
    String name;
    String img;
    Double price;
    @Column(name = "Createdate")
    @DateTimeFormat(pattern = "dd-mm-yyyy")
    Date createDate ;
    boolean deprecated;
    String description;
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryID")
    Categories category;
    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    List<Order_Details> orderDetails;
    public Products(String productID, String name, String img, Double price, Date createDate, boolean deprecated, String description, Categories category, List<Order_Details> orderDetails) {
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
    public Products() {
    }
}
