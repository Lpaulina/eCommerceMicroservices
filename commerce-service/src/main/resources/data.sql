
-- ========================
-- Insert Products
-- ========================
INSERT INTO product (name, description, price, category, stock_quantity)
VALUES
    ( 'Nintendo Switch', 'Hybrid gaming console', 299.99, 'Electronics', 25),
    ( 'Wireless Controller', 'Ergonomic game controller', 59.99, 'Accessories', 50),
    ( 'Zelda: Tears of the Kingdom', 'Adventure game', 69.99, 'Games', 40),
    ( 'Mario Kart 8 Deluxe', 'Racing game', 59.99, 'Games', 35)
    ON CONFLICT (id) DO NOTHING;

-- ========================
-- Insert Orders
-- ========================
INSERT INTO customer_order ( customer_id, order_date, status, total_amount, total_price)
VALUES
    ( 1, NOW(), 'PROCESSING', 2, 359.98),
    ( 2, NOW(), 'DELIVERED', 1, 299.99)
    ON CONFLICT (id) DO NOTHING;

-- ========================
-- Insert Order Items
-- ========================
INSERT INTO order_item ( product_id, order_id, quantity, price)
VALUES
    ( 1, 1, 1, 299.99),  -- Nintendo Switch for Alice
    ( 2, 1, 1, 59.99),   -- Wireless Controller for Alice
    ( 1, 2, 1, 299.99)  -- Nintendo Switch for Bob
    ON CONFLICT (id) DO NOTHING;