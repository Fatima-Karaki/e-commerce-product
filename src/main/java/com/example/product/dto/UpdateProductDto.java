package com.example.product.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateProductDto
{
	private String name;

	private String description;

	private Double price;
}
