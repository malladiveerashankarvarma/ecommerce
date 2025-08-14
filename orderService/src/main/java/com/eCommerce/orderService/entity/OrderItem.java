package com.eCommerce.orderService.entity;

// src/main/java/com/eCommerce/orderService/entity/OrderItem.java

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name = "order_items")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false) private Long productId;
    @Column(nullable = false) private String productName;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal price;
    @Column(nullable = false) private Integer quantity;
    private String description;
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	private String category;
    private String imageURL;

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Order getOrder(){return order;} public void setOrder(Order order){this.order=order;}
    public Long getProductId(){return productId;} public void setProductId(Long productId){this.productId=productId;}
    public String getProductName(){return productName;} public void setProductName(String productName){this.productName=productName;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal price){this.price=price;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer quantity){this.quantity=quantity;}
}
