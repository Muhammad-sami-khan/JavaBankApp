
# Bank Application 

## Overview
This is an Bank Application developed in Java and connected to a MySQL database. The system allows users to view their balance, withdraw amount, deposit amount, send money to another account, and view their transaction history.

## Database Structure
The MySQL database contains three tables: `UserInfo`, `Transactions`, and `Transfer`.

- `UserInfo`: Stores information about the user, including account number, pin code, account name, and balance.
- `Transactions`: Includes transaction ID (primary key), type, amount, and account number (foreign key from `UserInfo`) this table will be record all the transactions on each transaction .
- `Transfer`: Contains details like sender account number, recipient account number, and amount. It also creates a relationship with the `Transactions` table.

## Database Connection
In the `Main.java` file, you'll need to set up your MySQL database connection. Replace `'username'` and `'password'` with your MySQL username and password:

```java
static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 
static final String DB_URL = "jdbc:mysql://localhost/Bank_database";
static final String USER = "username";
static final String PASS = "password";


## Application Structure
The Java application includes two main files: `Bank.java` and `Main.java`.

- `Bank.java`: Contains methods for various  operations like `viewBalance`, `withdrawAmount`, `depositAmount`, `sendMoney`, and `transactionHistory`.
- `Main.java`: Contains the main method to run the application.

## Usage
When the application runs, the user will be presented with a menu of options:

1. View Balance
2. Withdraw Amount
3. Deposit Amount
4. Send Money
5. View Transaction History
6. Exit

The user can enter the corresponding number to perform the desired operation.


## Compilation and Execution
To compile and run the program, you will need to have the Java Development Kit (JDK) and MySQL Connector/J (jar) (a JDBC driver for MySQL) installed on your system. 

1. Open a terminal/command prompt.
2. Navigate to the directory containing the Java files (e.g., `E:\Bank_jdbc`).
3. Compile all the Java files using the `javac` command:
    ```
    Bank_jdbc> javac *.java
    ```
4. Run the `Main` class using the `java` command. Make sure to include the MySQL Connector/J JAR file in the classpath:
    ```
    java -cp ".;C:\mysql-connector-j-8.4.0.jar" Main
    ```
Please replace `C:\mysql-connector-j-8.4.0.jar` with the path to the MySQL Connector/J JAR file on your system.


## Future Work
Future updates will include more features and improvements to the Bank system.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
MIT
