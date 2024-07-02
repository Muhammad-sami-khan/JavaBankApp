import java.sql.*;

public class Bank {

    // This method is used to retrieve and display the balance of a specific account
    // from the database.
    public void viewBalance(Connection conn, int accNum) throws SQLException {
        String sql = "SELECT balance FROM UserInfo WHERE accNum = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accNum);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Your current balance is: $" + balance);
            } else {
                System.out.println("Account not found.");
            }
        }
    }

    /*
     * This method is used to withdraw a specified amount from a specific account.
     * It checks if the account has sufficient balance before making the withdrawal.
     */
    public void withdrawAmount(Connection conn, int accNum, double withdrawAmount) throws SQLException {
        String checkBalanceQuery = "SELECT balance FROM UserInfo WHERE accNum = ?";
        String updateBalanceQuery = "UPDATE UserInfo SET balance = balance - ? WHERE accNum = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkBalanceQuery);
                PreparedStatement updateStmt = conn.prepareStatement(updateBalanceQuery)) {
            checkStmt.setInt(1, accNum);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (withdrawAmount > balance) {
                    System.out.println("Sorry, insufficient balance.");
                }
                else if(withdrawAmount<0){
                    System.out.println("please Enter positive values");
                }

                else {
                    updateStmt.setDouble(1, withdrawAmount);
                    updateStmt.setInt(2, accNum);
                    updateStmt.executeUpdate();
                    System.out.println("Withdrawal successful: $" + withdrawAmount);
                    recordTransaction(conn, accNum, "Withdrawal", withdrawAmount);
                }
            } else {
                System.out.println("Account not found.");
            }
        }
    }

    // This method is used to deposit a specified amount into a specific account in
    // the database.
    public void depositAmount(Connection conn, int accNum, double depositAmount) throws SQLException {
        String updateBalanceQuery = "UPDATE UserInfo SET balance = balance + ? WHERE accNum = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateBalanceQuery)) {
            updateStmt.setDouble(1, depositAmount);
            updateStmt.setInt(2, accNum);
            updateStmt.executeUpdate();
            System.out.println("Deposit successful: $" + depositAmount);
            recordTransaction(conn, accNum, "Deposit", depositAmount);
        }
    }

    // This method retrieves and displays the transaction history of a specific
    // account from the database.
    public void transactionHistory(Connection conn, int accNum) throws SQLException {
        String query = "SELECT * FROM Transactions WHERE AccountNum = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, accNum);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("\t\t\tTransaction Type: " + rs.getString("type"));
                System.out.println("\t\t\tTransaction Amount: $" + rs.getDouble("transaction_amount"));
                System.out.println("\t\t\tTransaction Date: " + rs.getTimestamp("timestamp"));
                System.out.println("\t\t\t-----------------------------");
            }
        }
    }

    // This method records a transaction (deposit, withdrawal, or transfer) for a
    // specific account in the database.
    public void recordTransaction(Connection conn, int accNum, String type, double amount) throws SQLException {
        String insertQuery = "INSERT INTO Transactions (AccountNum, type, transaction_amount, timestamp) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setInt(1, accNum);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();
        }
    }

    /*
     * This method transfers a specified amount of money from a sender's account to
     * a recipient's account.
     * It checks if the sender's account has sufficient balance before making the
     * transfer.
     */
    public void sendMoney(Connection conn, int sender_accnum, int recipientAccNum, double amount) throws SQLException {
        String checkBalanceQuery = "SELECT balance FROM UserInfo WHERE accNum = ?";
        String updateSenderBalanceQuery = "UPDATE UserInfo SET balance = balance - ? WHERE accNum = ?";
        String updateRecipientBalanceQuery = "UPDATE UserInfo SET balance = balance + ? WHERE accNum = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkBalanceQuery);
                PreparedStatement updateSenderStmt = conn.prepareStatement(updateSenderBalanceQuery);
                PreparedStatement updateRecipientStmt = conn.prepareStatement(updateRecipientBalanceQuery)) {

            // Check if sender has sufficient balance
            checkStmt.setInt(1, sender_accnum);
            ResultSet senderRs = checkStmt.executeQuery();
            if (senderRs.next()) {
                double senderBalance = senderRs.getDouble("balance");
                if (amount > senderBalance) {
                    System.out.println("Insufficient balance!");
                    return;
                }
            } else {
                System.out.println("Sender account not found.");
                return;
            }

            // Update sender's balance
            updateSenderStmt.setDouble(1, amount);
            updateSenderStmt.setInt(2, sender_accnum);
            updateSenderStmt.executeUpdate();

            // Update recipient's balance
            updateRecipientStmt.setDouble(1, amount);
            updateRecipientStmt.setInt(2, recipientAccNum);
            updateRecipientStmt.executeUpdate();

            // Record transaction for sender
            recordTransaction(conn, sender_accnum, "Transfer", amount);

            // Record transaction for recipient
            recordTransaction(conn, recipientAccNum, "recived", amount);

            System.out.println(amount + " sent successfully!");
        }
    }
}
