package com.example.product.controller;

import com.example.product.common.Response;
import com.example.product.dto.AddProductDto;
import com.example.product.dto.UpdateProductDto;
import com.example.product.dto.UpdateStockDto;
import com.example.product.entity.ProductEntity;
import com.example.product.service.ProductService;
import jakarta.persistence.Lob;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/product")
@RestController
@RequiredArgsConstructor
public class ProductController
{
	private final ProductService productService;
	@PostMapping
	public ResponseEntity<Response> addProduct(@RequestBody AddProductDto productDto){
		Response response = productService.addProduct(productDto);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<Response> getProducts(@RequestParam Integer limit, @RequestParam Integer offset){
		Response response = productService.getProducts(limit, offset);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{productId}")
	public ResponseEntity<Response> updateProduct(@PathVariable Long productId,  @RequestBody UpdateProductDto productDto){
		Response response = productService.updateProduct(productId, productDto);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/stock/{productId}")
	public ResponseEntity<Response> updateProductStock(@PathVariable Long productId, @RequestBody UpdateStockDto productDto){
		Response response = productService.updateProductStock(productId, productDto);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<Response> deleteProduct(@PathVariable Long productId){
		Response response = productService.deleteProduct(productId);
		return ResponseEntity.ok(response);
	}

}
