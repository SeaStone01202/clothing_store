-- Thêm dữ liệu vào bảng categories
INSERT INTO categories (name, status, created_at) VALUES
                                                      ('Áo', 'ACTIVE', '2025-03-23'),
                                                      ('Quần', 'ACTIVE', '2025-03-23'),
                                                      ('Giày', 'ACTIVE', '2025-03-23'),
                                                      ('Phụ Kiện', 'ACTIVE', '2025-03-23');

-- Thêm dữ liệu vào bảng products
-- Category: Áo (category_id = 1)
INSERT INTO products (name, price, stock, description, image_url, category_id) VALUES
                                                                                   ('Áo thun nam cổ tròn', 150000, 100, 'Áo thun nam cổ tròn, chất liệu cotton thoáng mát', 'https://example.com/images/ao-thun-nam.jpg', 1),
                                                                                   ('Áo sơ mi nam dài tay', 250000, 80, 'Áo sơ mi nam dài tay, phong cách lịch lãm', 'https://example.com/images/ao-so-mi-nam.jpg', 1),
                                                                                   ('Áo polo nam cao cấp', 200000, 120, 'Áo polo nam, chất liệu cao cấp, thoải mái', 'https://example.com/images/ao-polo-nam.jpg', 1),
                                                                                   ('Áo khoác nam thể thao', 350000, 60, 'Áo khoác nam phong cách thể thao, chống thấm nước', 'https://example.com/images/ao-khoac-nam.jpg', 1),
                                                                                   ('Áo hoodie nam unisex', 300000, 90, 'Áo hoodie nam unisex, phong cách trẻ trung', 'https://example.com/images/ao-hoodie-nam.jpg', 1);

-- Category: Quần (category_id = 2)
INSERT INTO products (name, price, stock, description, image_url, category_id) VALUES
                                                                                   ('Quần jeans nam slim fit', 400000, 70, 'Quần jeans nam slim fit, phong cách hiện đại', 'https://example.com/images/quan-jeans-nam.jpg', 2),
                                                                                   ('Quần kaki nam công sở', 300000, 85, 'Quần kaki nam công sở, thoải mái, lịch sự', 'https://example.com/images/quan-kaki-nam.jpg', 2),
                                                                                   ('Quần short nam thể thao', 150000, 110, 'Quần short nam thể thao, chất liệu thoáng mát', 'https://example.com/images/quan-short-nam.jpg', 2),
                                                                                   ('Quần jogger nam năng động', 250000, 95, 'Quần jogger nam, phong cách năng động', 'https://example.com/images/quan-jogger-nam.jpg', 2),
                                                                                   ('Quần tây nam cao cấp', 350000, 65, 'Quần tây nam cao cấp, phù hợp sự kiện', 'https://example.com/images/quan-tay-nam.jpg', 2);

-- Category: Giày (category_id = 3)
INSERT INTO products (name, price, stock, description, image_url, category_id) VALUES
                                                                                   ('Giày thể thao nam trắng', 500000, 50, 'Giày thể thao nam màu trắng, phong cách trẻ trung', 'https://example.com/images/giay-the-thao-trang.jpg', 3),
                                                                                   ('Giày da nam công sở', 700000, 40, 'Giày da nam công sở, chất liệu da thật', 'https://example.com/images/giay-da-nam.jpg', 3),
                                                                                   ('Giày sneaker nam đen', 450000, 60, 'Giày sneaker nam màu đen, phong cách năng động', 'https://example.com/images/giay-sneaker-den.jpg', 3),
                                                                                   ('Giày lười nam cao cấp', 600000, 45, 'Giày lười nam, chất liệu cao cấp, thoải mái', 'https://example.com/images/giay-luoi-nam.jpg', 3),
                                                                                   ('Giày chạy bộ nam', 550000, 55, 'Giày chạy bộ nam, hỗ trợ êm chân', 'https://example.com/images/giay-chay-bo-nam.jpg', 3);

-- Category: Phụ Kiện (category_id = 4)
INSERT INTO products (name, price, stock, description, image_url, category_id) VALUES
                                                                                   ('Mũ lưỡi trai nam', 100000, 150, 'Mũ lưỡi trai nam, phong cách trẻ trung', 'https://example.com/images/mu-luoi-trai.jpg', 4),
                                                                                   ('Thắt lưng da nam', 200000, 90, 'Thắt lưng da nam, chất liệu da thật', 'https://example.com/images/that-lung-da.jpg', 4),
                                                                                   ('Ví da nam cao cấp', 250000, 80, 'Ví da nam cao cấp, thiết kế sang trọng', 'https://example.com/images/vi-da-nam.jpg', 4),
                                                                                   ('Kính râm nam thời trang', 300000, 70, 'Kính râm nam, phong cách thời trang', 'https://example.com/images/kinh-ram-nam.jpg', 4),
                                                                                   ('Dây đồng hồ da', 150000, 100, 'Dây đồng hồ da, chất liệu bền bỉ', 'https://example.com/images/day-dong-ho-da.jpg', 4);