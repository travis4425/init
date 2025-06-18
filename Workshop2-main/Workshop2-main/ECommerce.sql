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

-- 2. Ngành hàng
CREATE TABLE tblCategories (
    categoryID INT PRIMARY KEY IDENTITY(1,1),
    categoryName NVARCHAR(100),
    description NVARCHAR(255)
);
GO

-- 3. Sản phẩm
CREATE TABLE tblProducts (
    productID INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100),
    categoryID INT,
    price FLOAT,
    quantity INT,
    sellerID VARCHAR(20),
    status VARCHAR(20),
    FOREIGN KEY (categoryID) REFERENCES tblCategories(categoryID),
    FOREIGN KEY (sellerID) REFERENCES tblUsers(userID)
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
    content TEXT,
    status VARCHAR(50),
    reply TEXT,
    FOREIGN KEY (userID) REFERENCES tblUsers(userID)
);
GO

insert into tblUsers (userID, fullName, roleID, password, phone) values ('ltk', N'Lương Tuấn Kiệt', 'AD', '1', '0888819044');