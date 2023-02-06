package com.example.banking_transaction;

import java.util.ArrayList;
import java.util.HashMap;


public class AccountDB {
    /* Class consists of all accounts stored in a hashmap including:
        - the opening and deletion of accounts
        - the generated unique account number for the new account
     */
    static HashMap<String, BankAccount> accountDatabase = new HashMap<>();


    // This method is for accepting new accounts and storing it in a database (HashMap structure)
    static void openAccount(String accountNumber,
                            String govId,
                            HashMap<String, String> name,
                            double initialDeposit,
                            long pin) {
        BankAccount newAcc = new BankAccount(accountNumber, govId, name, initialDeposit);
        newAcc.generatePIN(pin);
        accountDatabase.put(accountNumber, newAcc);
    }

    // Account Numbers will have 8 digits. When account is registered or opened, new account will use
    //    the current availableAccountNumber and increment it for the next new account
    private static int availableAccountNum = 12100000;
    // every new account number will be unique,
    // thus loop is considered to check if the generated number is already used
    static String generateAccountNumber() {
        int num;
        do {
            num = ++availableAccountNum;
        } while (accountDatabase.containsKey(num));
        return String.valueOf(num);

    }

    static boolean checkAccount(String inputAccNum, String inputPIN) {
        if (accountDatabase.containsKey(inputAccNum)) {
            return accountDatabase.get(inputAccNum).getPIN().equals(inputPIN);
        }
        return false;
    }

    static boolean checkGovIdAlreadyExisting(String userInputGovID) {
        for (BankAccount account: accountDatabase.values()) {
            if (account.getGovID().equals(userInputGovID)) {
                return true;
            }
        }
        return false;
    }


    static String loggedAccount = "00000000";

    // Getters for specific BankAccounts ------------------------
        /*static ArrayList getArrayListTransaction() {
            return accountDatabase.get(loggedAccount).getTransactions();
        }*/
    static String getAccountNumber() {
        return accountDatabase.get(loggedAccount).getAccountNumber();
    }
    static String getBalance() {
        return String.valueOf(accountDatabase.get(loggedAccount).getAccountBalance());
    }
    static String getFullName() {
        return accountDatabase.get(loggedAccount).getFullName();
    }
    static String getGovID() {
        return accountDatabase.get(loggedAccount).getGovID();
    }
    // end of Getters -------------------------------------------
    static void accountDeposit(double amount) {
        accountDatabase.get(loggedAccount).deposit(amount);
    }
    static void accountWithdraw(double amount) {
        accountDatabase.get(loggedAccount).withdraw(amount);
    }

}

class BankAccount {
    /* Class consists of all account information of a user including:
        - withdraw and deposit method in account balance
        - list of transactions using the program
       Class constructor is for setting all account information
     */
    private String accountNumber = "0000";
    private String fullName;
    private double accountBalance = 0;
    private long pin;

    private String firstName;
    private String lastName;
    private String govId;

    // ArrayList holds all the transactions of the user account
        //private ArrayList transactions = new ArrayList();


    BankAccount(String accountNumber,
                String id,
                HashMap<String, String> name,
                double initialDeposit) {
        this.accountNumber = accountNumber;
        this.govId = id;
        this.firstName = name.get("firstname");
        this.lastName = name.get("lastname");
        this.accountBalance = initialDeposit;

        this.fullName = firstName + " " + lastName;
    }
    void generatePIN(long pin) {
        this.pin = pin;
    }

    // Getters --------------------------------------------------
        //public ArrayList getTransactions() { return transactions; }
    public String getAccountNumber() { return accountNumber; }
    public String getFullName() { return fullName; }
    public double getAccountBalance() { return accountBalance; }
    public String getGovID() { return govId; }
    public String getPIN() { return String.valueOf(pin); }
    // end of Getters -------------------------------------------

    // Methods of Deposit and Withdrawal
    void deposit(double amount) {
        accountBalance += amount;
        //transactions.add(new Transaction("Deposit", amount, accountBalance));
    }
    void withdraw(double amount) {
        accountBalance -= amount;
        //transactions.add(new Transaction("Withdraw", amount, accountBalance));
    }

    // NOTE: The transactions.add code will be added soon
}

/*
class Transaction {
     Class is for creating one transaction when user deposits or withdraws.
       The constructor of this class will be called in either of the two method in BankAccount class


    private String type; // of transaction (deposit or withdrawal)
    private double amount;
    private double balance; // new balance
    private String date;
    private String time;

    Transaction(String type, double amount, double balance) {
        this.date = String.valueOf(java.time.LocalDate.now());
        this.time = String.valueOf(java.time.LocalTime.now());
        this.type = type;
        this.amount = amount;
        this.balance = balance;
    }


    // Getters --------------------------------------------------
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalance() { return balance; }
    // end of Getters -------------------------------------------
}
*/
