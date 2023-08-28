package com.store.DTO;

import com.store.model.Products;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
public class ProductImgDTO {
    @ManyToOne
    @JoinColumn(name = "productID")
    Products product;
    long id;
    String Color;
    String ColorHex;
    MultipartFile img;
    int available;
    long imgid;
    int colorid;

    public ProductImgDTO(Products product, String color, int colorid, String colorHex, MultipartFile img) {
        this.product = product;
        Color = color;
        ColorHex = colorHex;
        this.img = img;
        this.colorid = colorid;
    }
//    public ProductImgDTO(Products product,int available, String color, String colorHex){
//        this.product = product;
//        Color = color;
//        this.available = available;
//        ColorHex = colorHex;
//
//    }
    public ProductImgDTO() {
    }
}
