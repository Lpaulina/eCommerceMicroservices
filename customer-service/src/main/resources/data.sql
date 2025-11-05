-- ========================
-- Insert Customers
-- ========================
INSERT INTO customer ( name, email, phone_number, password, street, city, state, zip, country)
VALUES
    ( 'Alice Johnson', 'alice@example.com', '555-1234', 'password123', '123 Main St', 'Tucson', 'AZ', '85721', 'USA'),
    ( 'Bob Smith', 'bob@example.com', '555-5678', 'password123', '456 Oak Ave', 'Phoenix', 'AZ', '85001', 'USA'),
    ( 'Charlie Brown', 'charlie@example.com', '555-9101', 'password123', '789 Pine Rd', 'Flagstaff', 'AZ', '86001', 'USA')
    ON CONFLICT (id) DO NOTHING;

-- ========================
-- Insert Notifications
-- ========================
INSERT INTO notification ( customer_id, type, message, status, sent_date)
VALUES
    ( 1, 'ORDER_CONFIRMATION', 'Your order has been confirmed!', 'SENT', NOW()),
    ( 2, 'DELIVERY', 'Your package is on the way!', 'SENT', NOW()),
    ( 3, 'PROMOTION', 'Get 20% off your next purchase!', 'SENT', NOW())
    ON CONFLICT (id) DO NOTHING;