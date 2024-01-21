package com.example.product.controller;

import com.example.product.common.Response;
import com.example.product.dto.AddProductDto;
import com.example.product.dto.UpdateProductDto;
import com.example.product.dto.UpdateStockDto;
import com.example.product.service.ProductService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RequestMapping("/product")
@RestController
public class ProductController
{

	private final ProductService productService;
	private final Bucket bucket;

	public ProductController(ProductService productService)
	{
		this.productService = productService;
		Bandwidth limitRate = Bandwidth.classic(3, Refill.greedy(20, Duration.ofMinutes(1))); // limit 30 in 1 min
		this.bucket = Bucket.builder().addLimit(limitRate).build();
	}

	@PostMapping
	public ResponseEntity<Response> addProduct(@RequestBody AddProductDto productDto)
	{
		if(bucket.tryConsume(1))
		{
			Response response = productService.addProduct(productDto);
			return ResponseEntity.ok(response);
		}
		else
		{
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new Response().setResult("Too Many Requests"));
		}
	}

	@GetMapping
	public ResponseEntity<Response> getProducts(@RequestParam Integer limit, @RequestParam Integer offset)
	{
		if(bucket.tryConsume(1))
		{
			Response response = productService.getProducts(limit, offset);
			return ResponseEntity.ok(response);
		}
		else
		{
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new Response().setResult("Too Many Requests"));
		}
	}

	@PutMapping("/{productId}")
	public ResponseEntity<Response> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductDto productDto)
	{
		if(bucket.tryConsume(1))
		{
			Response response = productService.updateProduct(productId, productDto);
			return ResponseEntity.ok(response);
		}
		else
		{
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new Response().setResult("Too Many Requests"));
		}
	}

	@PutMapping("/stock/{productId}")
	public ResponseEntity<Response> updateProductStock(@PathVariable Long productId, @RequestBody UpdateStockDto productDto)
	{
		if(bucket.tryConsume(1))
		{
			Response response = productService.updateProductStock(productId, productDto);
			return ResponseEntity.ok(response);
		}
		else
		{
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new Response().setResult("Too Many Requests"));
		}
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<Response> deleteProduct(@PathVariable Long productId)
	{
		if(bucket.tryConsume(1))
		{
			Response response = productService.deleteProduct(productId);
			return ResponseEntity.ok(response);
		}
		else
		{
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new Response().setResult("Too Many Requests"));
		}
	}

}
