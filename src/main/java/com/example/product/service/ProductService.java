package com.example.product.service;

import com.example.product.common.Response;
import com.example.product.common.UpdateActionEnum;
import com.example.product.dto.AddProductDto;
import com.example.product.dto.UpdateProductDto;
import com.example.product.dto.UpdateStockDto;
import com.example.product.entity.ProductAuditLogEntity;
import com.example.product.entity.ProductEntity;
import com.example.product.repository.ProductAuditLogRepository;
import com.example.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService
{

	private final ProductRepository productRepository;
	private final ProductAuditLogRepository productAuditLogRepository;

	public Response addProduct(AddProductDto productDto)
	{
		Response response = new Response();
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(productDto, productEntity);
		ProductEntity product = productRepository.save(productEntity);

		response.setResult(product).setStatus("Success").setStatusCode(200);
		return response;
	}

	public Response updateProduct(Long productId, UpdateProductDto productDto)
	{
		Optional<ProductEntity> productEntity = productRepository.findById(productId);
		if(productEntity.isEmpty())
		{
			throw new RuntimeException("Product not found");
		}
		ProductEntity product = productEntity.get();

		Optional.ofNullable(productDto.getName()).ifPresent(product::setName);
		Optional.ofNullable(productDto.getDescription()).ifPresent(product::setDescription);
		Optional.ofNullable(productDto.getPrice()).ifPresent(product::setPrice);

		productRepository.save(product);

		ProductAuditLogEntity productAuditLogEntity = new ProductAuditLogEntity();
		productAuditLogEntity.setProductId(productId).setActionType(UpdateActionEnum.UPDATE_DETAILS.toString()).setCreatedAt(LocalDateTime.now());
		productAuditLogRepository.save(productAuditLogEntity);

		Response response = new Response();
		response.setResult(product).setStatus("Success").setStatusCode(200);
		return response;
	}

	public Response updateProductStock(Long productId, UpdateStockDto productDto)
	{
		// this is used when adding stock, or when customer purchases should be updated b
		Optional<ProductEntity> productEntity = productRepository.findById(productId);
		if(productEntity.isEmpty())
		{
			throw new RuntimeException("Product not found");
		}
		ProductEntity product = productEntity.get();

		Integer stockQuantity = product.getStockQuantity();
		String action = this.validateUpdateStock(productDto);
		switch(action)
		{
		case "update" -> product.setStockQuantity(productDto.getStockQuantity());
		case "add" -> product.setStockQuantity(stockQuantity + productDto.getItemsAdded());
		case "remove" -> product.setStockQuantity(stockQuantity - productDto.getItemsRemoved());
		}

		productRepository.save(product);

		ProductAuditLogEntity productAuditLogEntity = new ProductAuditLogEntity();
		productAuditLogEntity.setProductId(productId)
							 .setActionType(UpdateActionEnum.UPDATE_STOCK.toString())
							 .setActionDetails(action)
							 .setCreatedAt(LocalDateTime.now());
		productAuditLogRepository.save(productAuditLogEntity);

		Response response = new Response();
		response.setResult(product).setStatus("Success").setStatusCode(200);
		return response;
	}

	public Response deleteProduct(Long productId)
	{
		Optional<ProductEntity> productEntity = productRepository.findById(productId);
		if(productEntity.isEmpty())
		{
			throw new RuntimeException("Product not found");
		}

		productRepository.deleteById(productId);
		Response response = new Response();
		response.setStatus("Success").setStatusCode(200);
		return response;
	}

	private String validateUpdateStock(UpdateStockDto updateStockDto)
	{

		int providedFieldsCount = 0;
		String action = null;

		if(updateStockDto.getStockQuantity() != null)
		{
			providedFieldsCount++;
			action = "update";
		}

		if(updateStockDto.getItemsAdded() != null)
		{
			providedFieldsCount++;
			action = "add";
		}

		if(updateStockDto.getItemsRemoved() != null)
		{
			providedFieldsCount++;
			action = "remove";
		}

		if(providedFieldsCount != 1)
		{
			throw new IllegalArgumentException("Exactly one field should be provided: stockQuantity, itemsAdded, or itemsRemoved");
		}

		return action;
	}

	public Response getProducts(Integer limit, Integer offset)
	{
		Pageable pageable = PageRequest.of(offset, limit);

		Page<ProductEntity> productEntities = productRepository.findAll(pageable);
		Response response = new Response();
		response.setResult(productEntities).setStatus("Success").setStatusCode(200);
		return response;
	}
}
