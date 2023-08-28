package com.store.DTO;

import com.store.model.Products;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductColorDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long colorID;
    String colorhex;
    int available;
    String color_name;
    String productID;
}
