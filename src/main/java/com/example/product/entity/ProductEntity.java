package com.example.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "product", schema = "shop")
@Entity
@Getter
@Setter
public class ProductEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	private String name;

	private String description;

	private Double price;

	@Column(name = "stock_quantity")
	private Integer stockQuantity;
}
