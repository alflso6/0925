package com.example.gagumoa_backend.service.scraper;

import com.example.gagumoa_backend.model.Product;
import com.example.gagumoa_backend.service.scraper.interface.Scraper;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HanssemScraper implements Scraper {

    @Override
    public String getBrandName() {
        return "Hanssem";
    }

    @Override
    public List<Product> scrapeProducts(WebDriver driver, String url, String category) {
        System.out.println("TODO: 한샘(" + category + ") 스크래핑 로직 구현 필요: " + url);
        List<Product> products = new ArrayList<>();

        // TODO: 1. driver.get(url)로 이동
        // TODO: 2. 한샘 사이트의 DOM 구조에 맞게 상품 목록을 파싱하는 WebDriver 코드를 구현합니다.
        
        // --- 임시 가상 데이터 ---
        if (category.equals("침대")) {
            Product tempProduct = new Product();
            tempProduct.setName("한샘 유로 501 침대");
            tempProduct.setBrand(getBrandName());
            tempProduct.setPrice(799000);
            // 프론트엔드의 이미지 프록시를 통해 로드될 가상 이미지 URL입니다.
            tempProduct.setImageUrl("/api/image-proxy?url=https://via.placeholder.com/240x160?text=Hanssem+Bed");
            tempProduct.setIkeaProductId(null); // IKEA 전용 ID는 null
            products.add(tempProduct);
        } else if (category.equals("의자")) {
            Product tempProduct = new Product();
            tempProduct.setName("한샘 조이S 의자");
            tempProduct.setBrand(getBrandName());
            tempProduct.setPrice(150000);
            tempProduct.setImageUrl("/api/image-proxy?url=https://via.placeholder.com/240x160?text=Hanssem+Chair");
            tempProduct.setIkeaProductId(null);
            products.add(tempProduct);
        }
        // -------------------------
        
        return products;
    }
}