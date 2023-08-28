	package com.store.model;

	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.JoinColumn;
	import javax.persistence.ManyToOne;
	import javax.persistence.Table;

	import lombok.Data;
	import lombok.Getter;
	import lombok.Setter;

	@SuppressWarnings("serial")
	@Getter
	@Setter
	@Entity @Table(name = "product_image")
	public class Product_Images {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		long imgID;
		String image;
		@ManyToOne
		@JoinColumn(name = "colorID")
		Product_Colors productcolor;

		public Product_Images(String image, Product_Colors productcolor) {
			this.image = image;
			this.productcolor = productcolor;
		}
		public Product_Images() {
		}
	}
