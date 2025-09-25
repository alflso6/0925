package com.example.gagumoa_backend.service.scraper;

import com.example.gagumoa_backend.model.Product;
import com.example.gagumoa_backend.service.scraper.interface.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class IkeaScraper implements Scraper {

    @Override
    public String getBrandName() {
        return "IKEA";
    }

    @Override
    public List<Product> scrapeProducts(WebDriver driver, String url, String category) {
        List<Product> products = new ArrayList<>();

        System.out.println(getBrandName() + " " + category + " 상품 스크래핑 시작: " + url);

        try {
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            try {
                // 쿠키 팝업 처리 (원래 로직에서 가져옴 - 실제 선택자로 변경 필요)
                WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("쿠키_팝업_수락_버튼의_CSS_선택자")));
                acceptButton.click();
            } catch (Exception e) {
                // 팝업이 없거나 처리할 수 없을 때 무시
            }

            // 상품 목록 로딩 대기 (원래 로직에서 가져옴)
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".plp-price-module__description")));

            List<WebElement> productElements = driver.findElements(By.cssSelector(".plp-product-list__products > div"));

            for (WebElement productElement : productElements) {
                try {
                    String name = productElement.findElement(By.cssSelector(".plp-price-module__description")).getText();
                    String priceText = productElement.findElement(By.cssSelector(".plp-price__integer")).getText();
                    // priceText에서 숫자만 추출
                    int price = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
                    String imageUrl = productElement.findElement(By.cssSelector("img.plp-product__image")).getDomAttribute("src");
                    
                    // IKEA 상품 ID 추출 로직 (현재는 임시로 비워둠 - 실제 웹에서 추출 로직 구현 필요)
                    String ikeaProductId = ""; 

                    Product product = new Product();
                    product.setName(name);
                    product.setBrand(getBrandName());
                    product.setPrice(price);
                    product.setImageUrl(imageUrl);
                    product.setIkeaProductId(ikeaProductId); 
                    products.add(product);
                } catch (Exception e) {
                    System.err.println(getBrandName() + " 상품 정보를 가져오는 중 오류 발생: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println(getBrandName() + " " + category + " 스크래핑 중 오류 발생: " + e.getMessage());
        }
        return products;
    }
}