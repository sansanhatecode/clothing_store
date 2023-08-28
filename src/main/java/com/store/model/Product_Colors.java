package com.store.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Entity @Table(name = "product_color")
public class Product_Colors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long colorID;
    int available;
    String colorhex;
    String color_name;
    @ManyToOne
    @JoinColumn(name = "productID")
    Products product;

    public Product_Colors(String colorhex, String color_name, Products product) {
        this.colorhex = colorhex;
        this.color_name = color_name;
        this.product = product;
    }
	public Product_Colors(String colorhex, int available, String color_name, Products product) {
		this.colorhex = colorhex;
		this.color_name = color_name;
		this.product = product;
		this.available = available;
	}
    public Product_Colors() {

    }
   }
