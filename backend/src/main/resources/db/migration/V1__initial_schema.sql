-- FarmFresh baseline schema (MySQL 8). Use a fresh database or backup before first migrate.

CREATE TABLE IF NOT EXISTS farmer (
    farmer_id INT NOT NULL AUTO_INCREMENT,
    address VARCHAR(200),
    email VARCHAR(25),
    firstname VARCHAR(20),
    lastname VARCHAR(20),
    phone_no VARCHAR(15),
    PRIMARY KEY (farmer_id),
    UNIQUE KEY uk_farmer_email (email),
    UNIQUE KEY uk_farmer_phone (phone_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS category (
    category_id INT NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(255),
    PRIMARY KEY (category_id),
    UNIQUE KEY uk_category_name (category_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS farm_user (
    user_id INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone_no VARCHAR(15),
    `Address` VARCHAR(200),
    firstname VARCHAR(20),
    lastname VARCHAR(20),
    is_admin BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS stock_details (
    product_id INT NOT NULL AUTO_INCREMENT,
    price_per_unit FLOAT,
    quantity INT NOT NULL,
    stock_item VARCHAR(50) NOT NULL,
    product_image VARCHAR(400),
    category_id INT,
    farmer_id INT,
    PRIMARY KEY (product_id),
    UNIQUE KEY uk_stock_item (stock_item),
    KEY idx_stock_farmer (farmer_id),
    KEY idx_stock_category (category_id),
    CONSTRAINT fk_stock_category FOREIGN KEY (category_id) REFERENCES category (category_id),
    CONSTRAINT fk_stock_farmer FOREIGN KEY (farmer_id) REFERENCES farmer (farmer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS orders (
    order_id INT NOT NULL AUTO_INCREMENT,
    delivery_status BIT(1),
    payment_status BIT(1),
    delivery_date DATE,
    place_order_date DATE,
    user_id INT NOT NULL,
    PRIMARY KEY (order_id),
    KEY idx_orders_user_date (user_id, place_order_date),
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES farm_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS order_details (
    id INT NOT NULL AUTO_INCREMENT,
    amount DOUBLE NOT NULL,
    order_item VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    farmer_id INT,
    order_id INT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_od_order (order_id),
    CONSTRAINT fk_od_farmer FOREIGN KEY (farmer_id) REFERENCES farmer (farmer_id),
    CONSTRAINT fk_od_order FOREIGN KEY (order_id) REFERENCES orders (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS shopping_cart (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_cart_user (user_id),
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES farm_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS shopping_cart_line (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cart_id BIGINT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_cart_product (cart_id, product_id),
    CONSTRAINT fk_line_cart FOREIGN KEY (cart_id) REFERENCES shopping_cart (id) ON DELETE CASCADE,
    CONSTRAINT fk_line_product FOREIGN KEY (product_id) REFERENCES stock_details (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
