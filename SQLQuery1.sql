USE LibraryDB;

CREATE TABLE Books (
    BookID INT PRIMARY KEY IDENTITY(1,1),
    Title NVARCHAR(100),
    Author NVARCHAR(100),
    Price FLOAT
);

INSERT INTO Books (Title, Author, Price)
VALUES ('Clean Code', 'Robert Martin', 250);

SELECT * FROM Books;