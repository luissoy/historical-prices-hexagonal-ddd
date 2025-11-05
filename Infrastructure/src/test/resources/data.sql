-- ===============================
-- Sample products
-- ===============================
INSERT INTO products (name, description)
VALUES
    ('Zapatillas deportivas', 'Modelo 2025 edición limitada'),
    ('Camiseta técnica', 'Tejido transpirable de alta calidad');

-- ===============================
-- Sample prices
-- ===============================
INSERT INTO prices (product_id, price_value, currency_code, init_date, end_date)
VALUES
    (1, 99.99, 'EUR', '2024-01-01T00:00:00', '2024-06-30T23:59:59'),
    (1, 109.99, 'EUR', '2024-07-01T00:00:00', NULL),
    (2, 29.99, 'EUR', '2024-02-01T00:00:00', '2024-12-31T23:59:59');
