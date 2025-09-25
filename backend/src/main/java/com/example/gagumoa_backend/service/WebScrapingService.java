package com.example.gagumoa_backend.service;

import com.example.gagumoa_backend.model.Product;
import com.example.gagumoa_backend.model.Review;
import com.example.gagumoa_backend.repository.ProductRepository;
import com.example.gagumoa_backend.service.scraper.interface.Scraper; // Scraper 인터페이스 추가
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WebScrapingService {

    private final ProductRepository productRepository;
    private final Map<String, Scraper> scrapers; // 브랜드 이름으로 Scraper를 찾기 위한 Map

    // 내부 클래스: 스크래핑 대상을 정의합니다. (브랜드, 카테고리, URL)
    private static class ScrapingTarget {
        final String brand;
        final String category;
        final String url;

        public ScrapingTarget(String brand, String category, String url) {
            this.brand = brand;
            this.category = category;
            this.url = url;
        }
    }

    // 모든 Scraper 구현체를 주입받고 Map으로 변환하여 사용합니다.
    public WebScrapingService(ProductRepository productRepository, List<Scraper> scraperList) {
        this.productRepository = productRepository;
        this.scrapers = scraperList.stream()
                                  .collect(Collectors.toMap(Scraper::getBrandName, Function.identity()));
    }

    // WebDriver 설정을 공통화합니다.
    private WebDriver createWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        // 드라이버 경로 설정이 필요할 수 있습니다. (예: System.setProperty("webdriver.chrome.driver", "path/to/chromedriver"))
        return new ChromeDriver(options);
    }
    
    // 상품 스크래핑 및 저장 (확장된 로직)
    public void scrapeAndSaveProducts() {
        if (productRepository.count() > 0) {
            System.out.println("데이터베이스에 이미 상품이 존재합니다. 스크래핑을 건너뜁니다.");
            return;
        }

        // 1. 스크래핑 대상 정의 (카테고리 및 브랜드 확장)
        List<ScrapingTarget> targets = List.of(
            // IKEA 상품: 침대, 의자, 책상, 소파
            new ScrapingTarget("IKEA", "침대", "https://www.ikea.com/kr/ko/search/?q=%EC%B9%A8%EB%8C%80"),
            new ScrapingTarget("IKEA", "의자", "https://www.ikea", "https://www.ikea.com/kr/ko/search/?q=%EC%9D%98%EC%9E%90"),
            new ScrapingTarget("IKEA", "책상", "https://www.ikea.com/kr/ko/search/?q=%EC%B1%85%EC%83%81"),
            new ScrapingTarget("IKEA", "소파", "https://www.ikea.com/kr/ko/search/?q=%EC%86%8C%ED%8C%8C"),
            
            // 한샘 상품 (가상 URL 및 카테고리)
            new ScrapingTarget("Hanssem", "침대", "https://www.hanssem.com/search?k=%EC%B9%A8%EB%8C%80"), 
            // 에이스 상품 (가상 URL 및 카테고리)
            new ScrapingTarget("ACE", "침대", "https://www.acebed.com/search?keyword=%EC%B9%A8%EB%8C%80")
        );

        WebDriver driver = createWebDriver();
        List<Product> allProducts = new ArrayList<>();

        try {
            for (ScrapingTarget target : targets) {
                Scraper scraper = scrapers.get(target.brand);
                
                if (scraper != null) {
                    List<Product> products = scraper.scrapeProducts(driver, target.url, target.category);
                    allProducts.addAll(products);
                } else {
                    System.err.println("경고: " + target.brand + "에 대한 스크래퍼를 찾을 수 없습니다.");
                }
            }
            
            // 기존 data.sql로 저장된 IKEA 데이터가 있으므로, 새로운 데이터는 주석 처리합니다.
            // 필요시 `data.sql`의 내용을 지우고 이 코드를 활성화하여 사용하세요.
            // productRepository.saveAll(allProducts);
            
            System.out.println("스크래핑한 총 상품 " + allProducts.size() + "개를 데이터베이스에 저장했습니다. (실제 DB 저장은 주석 처리됨)");

        } catch (Exception e) {
            System.err.println("웹 스크래핑 중 치명적인 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    // 리뷰 스크래핑은 IKEA 상품 ID를 기반으로 동작하며, 다른 브랜드 추가 시 확장 필요
    public List<Review> scrapeReviewsForProduct(String productId) {
        WebDriver driver = createWebDriver();
        List<Review> reviews = new ArrayList<>();
        // IKEA 상품 ID를 사용하여 URL을 구성합니다.
        // 참고: 이 로직을 다중 브랜드에 대해 확장하려면, Product 객체에서 브랜드 정보를 가져와
        // 해당 브랜드에 맞는 리뷰 스크래퍼를 호출하도록 수정해야 합니다.
        String url = "https://www.ikea.com/kr/ko/p/songesand-bed-frame-with-2-storage-boxes-white-luroey-s99240992/" + productId;

        try {
            driver.get(url);

            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                // 리뷰 섹션의 컨테이너를 찾습니다.
                WebElement reviewSection = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".ugc-rr-pip-fe-theatre__content-wrapper"))); 

                List<WebElement> reviewElements = reviewSection.findElements(By.cssSelector(".ugc-rr-pip-fe-text.ugc-rr-pip-fe-typography-body-m")); 
                for (WebElement reviewElement : reviewElements) {
                    try {
                        // 실제 리뷰 카드를 찾는 로직이 필요 (임시로 XPath를 사용하는 대신 CSS 선택자 기반으로 로직을 가정)
                        WebElement reviewCard = reviewElement.findElement(By.xpath("./ancestor::div[contains(@class, 'ugc-rr-pip-fe-review-card')]")); 

                        String author = reviewCard.findElement(By.cssSelector(".ugc-rr-pip-fe-text.ugc-rr-pip-fe-typography-caption-s")).getText(); 
                        String comment = reviewElement.getText(); 
                        String ratingText = reviewCard.findElement(By.cssSelector(".ugc-rr-pip-fe-rating_stars--small")).getDomAttribute("aria-label"); 
                        double rating = Double.parseDouble(ratingText.replaceAll("[^\\d.]", ""));
                        String date = reviewCard.findElement(By.cssSelector(".ugc-rr-pip-fe-text.ugc-rr-pip-fe-typography-caption-s.ugc-rr-pip-fe-text--light")).getText(); 

                        Review review = new Review();
                        review.setAuthor(author);
                        review.setRating(rating);
                        review.setComment(comment);
                        review.setDate(date);
                        reviews.add(review);
                    } catch (Exception reviewException) {
                        System.err.println("리뷰 정보를 가져오는 중 오류 발생: " + reviewException.getMessage());
                    }
                }
            } catch (Exception pageException) {
                System.err.println("리뷰 섹션을 찾을 수 없습니다. " + pageException.getMessage());
            }

        } catch (Exception e) {
            System.err.println("웹 스크래핑 중 치명적인 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
        return reviews;
    }
}