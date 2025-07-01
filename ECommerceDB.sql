CREATE DATABASE ECommerceDB;
GO
USE ECommerceDB;
GO

-- 1. Bảng người dùng
CREATE TABLE tblUsers (
    userID VARCHAR(20) PRIMARY KEY,
    fullName NVARCHAR(100),
    roleID VARCHAR(5),
    password NVARCHAR(100),
    phone VARCHAR(15)
);
GO

-- 4. Chương trình khuyến mãi
CREATE TABLE tblPromotions (
    promoID INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100),
    discountPercent FLOAT,
    startDate DATE,
    endDate DATE,
    status VARCHAR(20)
);
GO

-- 2. Ngành hàng
CREATE TABLE tblCategories (
    categoryID INT PRIMARY KEY IDENTITY(1,1),
    categoryName NVARCHAR(100),
    description NVARCHAR(255),
	promoID INT,
	FOREIGN KEY (promoID) REFERENCES tblPromotions(promoID)
);
GO

-- 3. Sản phẩm
CREATE TABLE tblProducts (
    productID INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100),
    categoryID INT,
    price FLOAT,
	originalPrice FLOAT,
    quantity INT,
    sellerID VARCHAR(20),
	promoID INT,
    status VARCHAR(20),
	FOREIGN KEY (promoID) REFERENCES tblPromotions(promoID),
    FOREIGN KEY (categoryID) REFERENCES tblCategories(categoryID),
    FOREIGN KEY (sellerID) REFERENCES tblUsers(userID)
);
GO

-- 5. Giỏ hàng
CREATE TABLE tblCarts (
    cartID INT PRIMARY KEY IDENTITY(1,1),
    userID VARCHAR(20),
    createdDate DATE,
    FOREIGN KEY (userID) REFERENCES tblUsers(userID)
);
GO

CREATE TABLE tblCartDetails (
    cartID INT,
    productID INT,
    quantity INT,
    PRIMARY KEY (cartID, productID),
    FOREIGN KEY (cartID) REFERENCES tblCarts(cartID),
    FOREIGN KEY (productID) REFERENCES tblProducts(productID)
);
GO

-- 6. Hóa đơn
CREATE TABLE tblInvoices (
    invoiceID INT PRIMARY KEY IDENTITY(1,1),
    userID VARCHAR(20),
    totalAmount FLOAT,
    status VARCHAR(20),
    createdDate DATE,
    FOREIGN KEY (userID) REFERENCES tblUsers(userID)
);
GO

CREATE TABLE tblInvoiceDetails (
    invoiceID INT,
    productID INT,
    quantity INT,
    price FLOAT,
    PRIMARY KEY (invoiceID, productID),
    FOREIGN KEY (invoiceID) REFERENCES tblInvoices(invoiceID),
    FOREIGN KEY (productID) REFERENCES tblProducts(productID)
);
GO

-- 7. Giao hàng
CREATE TABLE tblDeliveries (
    deliveryID INT PRIMARY KEY IDENTITY(1,1),
    invoiceID INT,
    address NVARCHAR(255),
    deliveryDate DATE,
    status VARCHAR(50),
    FOREIGN KEY (invoiceID) REFERENCES tblInvoices(invoiceID)
);
GO

-- 8. Trả hàng
CREATE TABLE tblReturns (
    returnID INT PRIMARY KEY IDENTITY(1,1),
    invoiceID INT,
    reason NVARCHAR(255),
    status VARCHAR(50),
    FOREIGN KEY (invoiceID) REFERENCES tblInvoices(invoiceID)
);
GO

-- 9. Chăm sóc khách hàng
CREATE TABLE tblCustomerCares (
    ticketID INT PRIMARY KEY IDENTITY(1,1),
    userID VARCHAR(20),
    subject NVARCHAR(100),
    content NVARCHAR(100),
    status NVARCHAR(50),
    reply NVARCHAR(100),
    FOREIGN KEY (userID) REFERENCES tblUsers(userID)
);
GO

-- Tạo bảng kho hàng
CREATE TABLE tblInventories (
    inventoryID INT IDENTITY(1,1) PRIMARY KEY,
    warehouseID INT,
    productID INT,
    stockQuantity FLOAT,
    FOREIGN KEY (productID) REFERENCES tblProducts(productID)
);
GO

-- 1. tblUsers
INSERT INTO tblUsers (userID, fullName, roleID, password, phone) VALUES
('ltk', N'Lương Tuấn Kiệt', 'AD', '1', '0888819044'),
('seller01', N'Trần Thị B', 'SE', 'abc123', '0909123456'),
('na', N'Nguyễn Phạm Nhật Anh', 'SE', '1', '0359925825'),
('bu', N'Buyer Nguyễn', 'BU', '1', '0909123456'),
('dl', N'Deliver Trần', 'DL', '1', '0909123456'),
('mk', N'Market Lê', 'MK', '1', '0909123456'),
('cs', N'Cuscare Phạm', 'CS', '1', '0909123456');


-- 2. tblCategories
INSERT INTO tblCategories (categoryName, description) VALUES
(N'Điện thoại', N'Smartphone chính hãng'),
(N'Máy tính', N'Laptop, PC và linh kiện'),
(N'Gia dụng', N'Thiết bị sử dụng trong gia đình');

-- 3. tblPromotions
INSERT INTO tblPromotions (name, discountPercent, startDate, endDate, status) VALUES
(N'Summer Sale', 10.0, '2025-06-01', '2025-06-30', N'Active'),
(N'Back to School', 5.0, '2025-07-01', '2025-07-15', N'Active');

-- 4. tblProducts
INSERT INTO tblProducts (name, categoryID, price, originalPrice, quantity, sellerID, status, promoID) VALUES
(N'iPhone 15 Pro', 1, 289, 289, 20, 'seller01', N'Active', 1),
(N'Dell XPS 13', 2, 279, 279, 10, 'na', N'Active', 1),
(N'Nồi chiên không dầu', 3, 199, 199, 30, 'seller01', N'Inactive', NULL);


-- Thêm dữ liệu tồn kho
INSERT INTO tblInventories (warehouseID, productID, stockQuantity) VALUES
(1, 1, 6),
(1, 2, 5);












