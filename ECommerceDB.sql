USE master;
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = 'ECommerceDB')
BEGIN
    ALTER DATABASE ECommerceDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE ECommerceDB;
END
GO

CREATE DATABASE ECommerceDB;
GO

USE ECommerceDB;
GO

-- =============================================
-- TẠO BẢNG
-- =============================================

-- 1. tblUsers
CREATE TABLE tblUsers (
    userID VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    fullName NVARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address NVARCHAR(200),
    roleID VARCHAR(2) NOT NULL DEFAULT 'BU', -- AD, SE, BU, MK, AC, DL, CS
    status BIT DEFAULT 1, -- 1: Active, 0: Inactive
    createdDate DATE DEFAULT GETDATE(),
    lastLogin DATETIME,
    
    -- Constraints
    CONSTRAINT CK_Users_Role CHECK (roleID IN ('AD', 'SE', 'BU', 'MK', 'AC', 'DL', 'CS')),
    CONSTRAINT CK_Users_Email CHECK (email LIKE '%@%.%')
);

GO

-- 2. tblCategories
CREATE TABLE tblCategories (
    categoryID INT IDENTITY(1,1) PRIMARY KEY,
    categoryName NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(500),
    parentCategoryID INT NULL,
    status BIT DEFAULT 1,
    createdDate DATE DEFAULT GETDATE(),
    CONSTRAINT FK_Categories_Parent FOREIGN KEY (parentCategoryID) REFERENCES tblCategories(categoryID)
);
GO

-- 3. tblProducts
CREATE TABLE tblProducts (
    productID INT IDENTITY(1,1) PRIMARY KEY,
    productName NVARCHAR(200) NOT NULL,
    description NVARCHAR(1000),
    price DECIMAL(18,2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    categoryID INT NOT NULL,
    sellerID VARCHAR(50) NOT NULL,
    image VARCHAR(200),
    status BIT DEFAULT 1,
    createdDate DATE DEFAULT GETDATE(),
    modifiedDate DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Products_Category FOREIGN KEY (categoryID) REFERENCES tblCategories(categoryID),
    CONSTRAINT FK_Products_Seller FOREIGN KEY (sellerID) REFERENCES tblUsers(userID),
    CONSTRAINT CK_Products_Price CHECK (price >= 0),
    CONSTRAINT CK_Products_Quantity CHECK (quantity >= 0)
);
GO

-- (Tiếp tục với các bảng còn lại từ `tblPromotions` đến `tblCustomerCares` như trong script gốc...)
-- Vì lý do giới hạn ký tự, bạn có muốn mình gửi từng **file chia nhỏ** phần còn lại không?

---



-- Insert sample users
-- Insert sample users
INSERT INTO tblUsers (userID, password, fullName, email, phone, address, roleID) VALUES
('admin', '12345', N'Quản trị viên', 'admin@shop.com', '0901234567', N'Hà Nội', 'AD'),
('seller1', '1', N'Nguyễn Văn A', 'seller1@shop.com', '0901234568', N'TP.HCM', 'SE'),
('buyer1', '1', N'Trần Thị B', 'buyer1@gmail.com', '0901234569', N'Đà Nẵng', 'BU'),
('buyer2', '1', N'Lê Văn C', 'buyer2@gmail.com', '0901234570', N'Hải Phòng', 'BU');
GO

-- Insert sample categories
INSERT INTO tblCategories (categoryName, description) VALUES
(N'Điện thoại & Phụ kiện', N'Điện thoại thông minh và phụ kiện'),
(N'Laptop & Máy tính', N'Laptop, PC và linh kiện máy tính'),
(N'Thời trang Nam', N'Quần áo, giày dép nam'),
(N'Thời trang Nữ', N'Quần áo, giày dép nữ'),
(N'Gia dụng & Đời sống', N'Đồ gia dụng, nội thất');
GO

-- Insert sample products
INSERT INTO tblProducts (productName, description, price, quantity, categoryID, sellerID, image) VALUES
(N'iPhone 14 Pro Max', N'Điện thoại iPhone 14 Pro Max 128GB', 25000000, 50, 1, 'seller1', 'iphone14.jpg'),
(N'Samsung Galaxy S23', N'Điện thoại Samsung Galaxy S23 256GB', 18000000, 30, 1, 'seller1', 'galaxys23.jpg'),
(N'MacBook Pro M2', N'MacBook Pro 13 inch với chip M2', 35000000, 20, 2, 'seller1', 'macbook.jpg'),
(N'Dell XPS 13', N'Laptop Dell XPS 13 thế hệ mới', 28000000, 15, 2, 'seller1', 'dellxps.jpg'),
(N'Áo sơ mi nam', N'Áo sơ mi nam công sở cao cấp', 350000, 100, 3, 'seller1', 'aosomi.jpg');
GO
