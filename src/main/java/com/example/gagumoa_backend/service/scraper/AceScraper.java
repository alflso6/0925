package com.example.gagumoa_backend.service.scraper;

import com.example.gagumoa_backend.model.Product;
import com.example.gagumoa_backend.service.scraper.interface.Scraper;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AceScraper implements Scraper {

    @Override
    public String getBrandName() {
        return "ACE";
    }

    @Override
    public List<Product> scrapeProducts(WebDriver driver, String url, String category) {
        System.out.println("TODO: 에이스(" + category + ") 스크래핑 로직 구현 필요: " + url);
        List<Product> products = new ArrayList<>();

        // TODO: 1. driver.get(url)로 이동
        // TODO: 2. 에이스 사이트의 DOM 구조에 맞게 상품 목록을 파싱하는 WebDriver 코드를 구현합니다.

        // --- 임시 가상 데이터 ---
        if (category.equals("침대")) {
            Product tempProduct = new Product();
            tempProduct.setName("에이스 침대 L1");
            tempProduct.setBrand(getBrandName());
            tempProduct.setPrice(1200000);
            tempProduct.setImageUrl("/api/image-proxy?url=https://via.placeholder.com/240x160?text=Ace+Bed+L1");
            tempProduct.setIkeaProductId(null);
            products.add(tempProduct);
        } else if (category.equals("소파")) {
            Product tempProduct = new Product();
            tempProduct.setName("에이스 에어 소파");
            tempProduct.setBrand(getBrandName());
            tempProduct.setPrice(2100000);
            tempProduct.setImageUrl("/api/image-proxy?url=https://via.placeholder.com/240x160?text=Ace+Sofa");
            tempProduct.setIkeaProductId(null);
            products.add(tempProduct);
        }
        // -------------------------

        return products;
    }
}