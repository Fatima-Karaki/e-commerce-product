package com.example.product.common;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class Response
{
	private Object result;
	private String status;
	private Integer statusCode;
}
