-- ===============================
-- Schema for Historical Prices API
-- ===============================

DROP TABLE IF EXISTS prices;
DROP TABLE IF EXISTS products;

-- ===============================
-- PRODUCTS
-- ===============================
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL
);

-- ===============================
-- PRICES
-- ===============================
CREATE TABLE prices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    price_value DECIMAL(10, 2) NOT NULL,
    currency_code VARCHAR(10) NOT NULL DEFAULT 'EUR',
    init_date DATE NOT NULL,
    end_date DATE NULL,
    CONSTRAINT fk_price_product FOREIGN KEY (product_id)
        REFERENCES products (id)
        ON DELETE CASCADE
);

-- Indexes for faster historical lookup
CREATE INDEX idx_prices_product_id ON prices (product_id);
CREATE INDEX idx_prices_date_range ON prices (product_id, init_date, end_date);
