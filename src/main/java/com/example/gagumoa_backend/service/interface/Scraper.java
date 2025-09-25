package com.example.gagumoa_backend.service.scraper.interface;

import com.example.gagumoa_backend.model.Product;
import org.openqa.selenium.WebDriver;

import java.util.List;

public interface Scraper {

    /**
     * 이 스크래퍼가 담당하는 브랜드 이름을 반환합니다.
     */
    String getBrandName();

    /**
     * 특정 URL에서 상품 정보를 스크래핑하여 Product 객체 리스트를 반환합니다.
     */
    List<Product> scrapeProducts(WebDriver driver, String url, String category);
}