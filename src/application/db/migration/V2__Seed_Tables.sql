-- Seed users
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');
INSERT INTO users (username, password, role) VALUES ('Frankie', 'Frankie', 'customer');
INSERT INTO users (username, password, role) VALUES ('Manzar', 'Manzar', 'customer');
INSERT INTO users (username, password, role) VALUES ('Jamal', 'Jamal', 'customer');

-- Seed shipping fees
INSERT INTO shippingFees (country, fee) VALUES ('Canada', 20);
INSERT INTO shippingFees (country, fee) VALUES ('USA', 35);
INSERT INTO shippingFees (country, fee) VALUES ('UK', 50);
INSERT INTO shippingFees (country, fee) VALUES ('China', 70);
INSERT INTO shippingFees (country, fee) VALUES ('Australia', 80);

-- Seed products
INSERT INTO products (name, description, price, stock) VALUES ('Dark Chocolate Bar', 'Rich 70% cocoa dark chocolate', 5.99, 50);
INSERT INTO products (name, description, price, stock) VALUES ('Milk Chocolate Truffles', 'Creamy milk chocolate truffles box of 12', 12.99, 30);
INSERT INTO products (name, description, price, stock) VALUES ('White Chocolate Almonds', 'White chocolate coated almonds 200g', 8.49, 40);
INSERT INTO products (name, description, price, stock) VALUES ('Hazelnut Pralines', 'Belgian hazelnut praline selection 250g', 15.99, 25);
INSERT INTO products (name, description, price, stock) VALUES ('Cocoa Powder', 'Premium Dutch-process cocoa powder 400g', 9.99, 60);
INSERT INTO products (name, description, price, stock) VALUES ('Chocolate Spread', 'Smooth dark chocolate spread 350g', 6.49, 45);
INSERT INTO products (name, description, price, stock) VALUES ('Mint Chocolate Thins', 'After-dinner mint chocolate thins 200g', 7.99, 35);
INSERT INTO products (name, description, price, stock) VALUES ('Salted Caramel Bites', 'Chocolate coated salted caramel bites 150g', 10.49, 20);

-- Seed cart items (Frankie has items in cart)
INSERT INTO cart (userId, productId, quantity) VALUES (2, 1, 3);
INSERT INTO cart (userId, productId, quantity) VALUES (2, 4, 1);
INSERT INTO cart (userId, productId, quantity) VALUES (3, 2, 2);

-- Seed orders
INSERT INTO orders (userId, country, shippingFee, totalPrice, status, orderDate) VALUES (2, 'Canada', 20, 37.97, 'Delivered', '2025-03-15');
INSERT INTO orders (userId, country, shippingFee, totalPrice, status, orderDate) VALUES (3, 'USA', 35, 60.98, 'Shipped', '2025-04-01');
INSERT INTO orders (userId, country, shippingFee, totalPrice, status, orderDate) VALUES (2, 'Canada', 20, 35.98, 'Pending', '2025-04-10');
INSERT INTO orders (userId, country, shippingFee, totalPrice, status, orderDate) VALUES (4, 'China', 70, 86.49, 'Processing', '2025-04-08');

-- Seed order items
INSERT INTO orderItems (orderId, productId, quantity, price) VALUES (1, 1, 3, 5.99);
INSERT INTO orderItems (orderId, productId, quantity, price) VALUES (2, 2, 2, 12.99);
INSERT INTO orderItems (orderId, productId, quantity, price) VALUES (3, 4, 1, 15.99);
INSERT INTO orderItems (orderId, productId, quantity, price) VALUES (3, 6, 1, 6.49);
INSERT INTO orderItems (orderId, productId, quantity, price) VALUES (4, 4, 1, 15.99);