package com.example.product.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class UpdateStockDto
{
	private Integer stockQuantity;
	private Integer itemsAdded;
	private Integer itemsRemoved;
}
