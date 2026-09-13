CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    FullName NVARCHAR(100),
    Username NVARCHAR(50) UNIQUE,
    Password NVARCHAR(50),
	
);




