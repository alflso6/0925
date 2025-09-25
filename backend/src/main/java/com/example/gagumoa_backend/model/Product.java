package com.example.gagumoa_backend.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private int price;
    private String imageUrl; // 이미지 URL 필드 추가
    private String ikeaProductId; // 실제 IKEA 상품 ID 필드 추가

    // Lombok의 @Data 어노테이션이 자동으로 getter/setter를 생성해주므로
    // 추가적인 코드는 필요 없습니다.
}