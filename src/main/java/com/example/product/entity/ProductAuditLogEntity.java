package com.example.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name= "product_audit_log" , schema = "shop")
@Getter
@Setter
@Accessors(chain = true)
public class ProductAuditLogEntity
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "product_id")
	private Long productId;

	@Column(name = "action_type")
	private String actionType;

	@Column(name = "action_details")
	private String actionDetails;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

}
