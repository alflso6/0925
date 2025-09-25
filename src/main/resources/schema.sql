CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    brand VARCHAR(255),
    price INT,
    imageUrl VARCHAR(255),
    ikeaProductId VARCHAR(255)
);