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

        // TODO: 실제 한샘 사이트의 DOM 구조에 맞게 WebDriver 코드를 구현해야 합니다.
        // 예시: driver.get(url); ... findElement(By.cssSelector("...")) ...

        // 임시 가상 데이터 (실제 스크래핑 코드로 대체해야 합니다)
        if (category.equals("침대")) {
            Product tempProduct = new Product();
            tempProduct.setName("한샘 유로 501 침대");
            tempProduct.setBrand(getBrandName());
            tempProduct.setPrice(799000);
            tempProduct.setImageUrl("/api/image-proxy?url=https://via.placeholder.com/240x160?text=Hanssem+Bed");
            tempProduct.setIkeaProductId(null); 
            products.add(tempProduct);
        }
        
        return products;
    }
}