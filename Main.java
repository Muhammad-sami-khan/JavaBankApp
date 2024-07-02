import java.sql.*; // Importing SQL package for database operations
import java.util.Scanner; // Importing Scanner class for taking user inputs

public class Main {
    // Database driver and connection details
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/Bank_database";
    static final String USER = "username";
    static final String PASS = "password";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        Scanner userInput = new Scanner(System.in); // Scanner object for user input
        Bank bankApp = new Bank(); // Creating a new Bank object

        try {
            // Loading JDBC driver and establishing database connection
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Welcome message and user authentication
            System.out.println("\n\n\t\t||--Welcome to Halisam Bank!--||\n");
            System.out.print("Please enter your Account No.: ");
            int accNum = userInput.nextInt();
            System.out.print("Please enter your PIN: ");
            int pincode = userInput.nextInt();

            boolean runprogramAgain = true;
            while (runprogramAgain) {
                // Authenticate user
                if (authenticateUser(conn, accNum, pincode)) {
                    // User authenticated successfully, show menu options
                    System.out.println("\n\n\t\t||--Halisam Bank--||\n");
                    while (true) {
                        // Displaying menu options to the user
                        System.out.println("\n\t\t1) Check Balance");
                        System.out.println("\t\t2) Withdraw Amount");
                        System.out.println("\t\t3) Deposit Amount");
                        System.out.println("\t\t4) Send Money");
                        System.out.println("\t\t5) View Statement");
                        System.out.println("\t\t6) Exit");

                        System.out.print("\nEnter your choice: ");
                        int choice = userInput.nextInt();

                        // Perform action based on user's choice
                        switch (choice) {
                            case 1:
                                bankApp.viewBalance(conn, accNum); // Check balance
                                break;
                            case 2:
                                System.out.print("Enter withdrawal amount: ");
                                double withdrawAmount = userInput.nextDouble();
                                if (withdrawAmount <= 0) {
                                    System.out.println("Please Enter positive Amount!");
                                    break;
                                } else {
                                    bankApp.withdrawAmount(conn, accNum, withdrawAmount); // Withdraw amount
                                    break;
                                }
                            case 3:
                                System.out.print("Enter deposit amount: ");
                                double depositAmount = userInput.nextDouble();
                                if (depositAmount <= 0) {
                                    System.out.println("Please Enter postive values!");
                                    break;
                                } else {
                                    bankApp.depositAmount(conn, accNum, depositAmount); // Deposit amount
                                    break;
                                }
                            case 4:
                                System.out.println("Please Enter recipeint Account Number:");
                                int recipientAccNum = userInput.nextInt();
                                if (!checkAccExistence(conn, recipientAccNum)) {
                                    System.out.println("Account doesn't Exist in our Database");
                                    break;
                                } else {
                                    System.out.println("Enter amount to transfer");
                                    double amount = userInput.nextDouble();
                                    if (amount <= 0) {
                                        System.out.println("please Enter postive values!");
                                        break;
                                    } else {
                                        bankApp.sendMoney(conn, accNum, recipientAccNum, amount); // Send money
                                        break;
                                    }
                                }
                            case 5:
                                System.out.println("\n\t\t\t||-- Your Transaction History --||\n");
                                bankApp.transactionHistory(conn, accNum); // View transaction history
                                break;
                            case 6:
                                System.out.println("\n\n\t||--Please collect your ATM card, Thank You!--||");
                                runprogramAgain = false; // Exit the program
                                break;
                            default:
                                System.out.println("Invalid choice: " + choice + ", please enter a valid choice!"); // Invalid
                                                                                                                    // choice
                                break;
                        }
                        if (!runprogramAgain) {
                            break; // this will break the inner loop
                        }
                    }
                } else {
                    System.out.println("\n\n\t||--Scuccessfully exit thank you for using our service!--||");
                    return; // this will break the outer loop
                }
            }
        } catch (SQLException se) {
            se.printStackTrace(); // Print SQL exception
        } catch (Exception e) {
            e.printStackTrace(); // Print general exception
        } finally {
            try {
                if (stmt != null)
                    stmt.close(); // Close statement
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close(); // Close connection
            } catch (SQLException se) {
                se.printStackTrace(); // Print SQL exception
            }
            userInput.close();
        }
    }

    // Method to authenticate user
    private static boolean authenticateUser(Connection conn, int accNum, int pincode) throws SQLException {
        String sql = "SELECT * FROM UserInfo WHERE accNum = ? AND pincode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accNum);
            pstmt.setInt(2, pincode);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Return true if user exists, false otherwise
        }
    }

    // Method to check account existence
    private static boolean checkAccExistence(Connection conn, int accNum) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM UserInfo WHERE accNum = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, accNum);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0; // Return true if account exists, false otherwise
            }
        }
        return false;
    }
}
