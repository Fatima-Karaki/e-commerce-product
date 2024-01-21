package com.example.product.repository;

import com.example.product.entity.ProductAuditLogEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductAuditLogRepository extends CrudRepository<ProductAuditLogEntity, Long>
{
}
