package com.store.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailDto implements Serializable {

    private Long orderID;
    private String productID;
    private String nameProduct;
    private Long colorID;
    private String colorhex;
    private Double price;
    private Integer quantity;
    private String img;
    private String categoryID;
}
