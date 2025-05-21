package com.expensetracker;

import java.io.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExpenseTracker {
	 private static List<TransactionTracker> transactions = new ArrayList<>();
	    private static Scanner scanner = new Scanner(System.in);

	    public static void main(String[] args) {
	        while (true) {
	            System.out.println("\nChoose Menu:");
	            System.out.println("1.Add Income or Expenses");
	            System.out.println("2.Load Transactions from File");
	            System.out.println("3.View Monthly Summary");
	            System.out.println("4.Exit");
	            System.out.print("Select the option: ");

	            int choice = scanner.nextInt();
	            scanner.nextLine();

	            switch (choice) {
	                case 1: 
	                	addTransaction();
	                    break;
                    case 2: 
                    	loadFromFile();
                        break;
	                case 3: 
	                	viewMonthlySummary(); 
                        break;
	                case 4:
	                	System.out.println("Thank You.......");
	                	System.exit(0);
                        break;
	                default: 
	                	System.out.println("Invalid choice.");
	            }
	        }
	    }
        
	    ////Add income and expenses by category
	    private static void addTransaction() {
	        System.out.print("Enter Income or Expense");
	        String typeInput = scanner.nextLine().trim();

	        TransactionTracker.Type type;
	        String category = null;

	        if (typeInput.equalsIgnoreCase("Income")) {
	            type = TransactionTracker.Type.INCOME;
	            System.out.println("Choose Income Category:");
	            System.out.println("1. Salary");
	            System.out.println("2. Business");
	            System.out.print("Enter choice: ");
	            int choice = scanner.nextInt();
	            scanner.nextLine();
	            
	            switch (choice) {
	                case 1: 
	                	category = "Salary"; 
	                	break;
	                case 2: 
	                	category = "Business"; 
	                	break;
	                default:
	                    System.out.println("Invalid choice. Defaulting to 'Other'");
	                    category = "Other"; break;
	            }
	            
	        } else if (typeInput.equalsIgnoreCase("Expense")) {
	            type = TransactionTracker.Type.EXPENSE;
	            System.out.println("Choose Expense Category:");
	            System.out.println("1. Food");
	            System.out.println("2. Rent");
	            System.out.println("3. Travel");
	            System.out.print("Enter choice: ");
	            int choice = scanner.nextInt();
	            scanner.nextLine(); 
	            switch (choice) {
	                case 1:
	                	category = "Food"; 
	                	break;
	                case 2:
	                	category = "Rent"; 
	                	break;
	                case 3:
	                	category = "Travel";
	                	break;
	                default:
	                    System.out.println("Invalid choice");
	                    break;
	            }
	        } 
	        
	        else 
	        {
	            System.out.println("Please enter 'Income' or 'Expense'.");
	            return;
	        }

	        System.out.print("Enter Amount: ");
	        double amount = scanner.nextDouble();
	        scanner.nextLine(); 

	        LocalDate date = LocalDate.now();

	        TransactionTracker transactionDetails = new TransactionTracker(type, category, amount, date);
	        transactions.add(transactionDetails);
	        saveTransactionToFile(transactionDetails); 
	    }

	    ////Save data in a file
	    private static void saveTransactionToFile(TransactionTracker transaction) {
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactionDetails.txt", true))) { 
	            String line = transaction.getType() + "|" + transaction.getCategory() + "|" + transaction.getAmount() + "|" + transaction.getDate();
	            writer.write(line);
	            writer.newLine();
	        }
	        catch (IOException e) 
	        {
	            System.out.println("Error saving transaction to file: " + e.getMessage());
	        }
	    }
	    
	    
	    ////load data from a file
	    private static void loadFromFile() {
	        System.out.print("Enter filename to load transactions: ");
	        String filename = scanner.nextLine().trim();

	        transactions.clear();

	        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
	            String line;
	    

	            while ((line = reader.readLine()) != null) {
	                String[] parts = line.split("\\|");
	                if (parts.length != 4) {
	                    
	                    continue;
	                }

	                String typeStr = parts[0].trim();
	                String category = parts[1].trim();
	                String amountStr = parts[2].trim();
	                String dateStr = parts[3].trim();

	                TransactionTracker.Type type;
	                if (typeStr.equalsIgnoreCase("INCOME")) {
	                    type = TransactionTracker.Type.INCOME;
	                } else if (typeStr.equalsIgnoreCase("EXPENSE")) {
	                    type = TransactionTracker.Type.EXPENSE;
	                } else {
	                    System.out.println("invalid transaction type: " );
	                    continue;
	                }

	                double amount;
	                LocalDate date;
	                try {
	                    amount = Double.parseDouble(amountStr);
	                    date = LocalDate.parse(dateStr);
	                } catch (Exception e) {
	                    System.out.println("invalid amount/date: ");
	                    continue;
	                }

	                transactions.add(new TransactionTracker(type, category, amount, date));
	               
	            }

	           
	            System.out.println("Transactions:");
	            for (TransactionTracker t : transactions) {
	                System.out.printf("%s | %s | %.2f | %s%n",
	                    t.getType(), t.getCategory(), t.getAmount(), t.getDate());
	                
	            }
	            System.out.println("Transactions loaded successfully..... ");
	        } catch (IOException e) {
	            System.out.println("Error reading file '" + filename + "': " + e.getMessage());
	        }
	    }
	    
	    
	    ////View monthly summaries
	    private static void viewMonthlySummary() {
	        System.out.print("Enter year and month in format(yyyy-mm): ");
	        String input = scanner.nextLine().trim();
	        
	        String[] monthYear = input.split("-");
	        if (monthYear.length != 2) {
	            System.out.println("Invalid format. Please enter in yyyy-mm format.");
	            return;
	        }
	        
	        int year, month;
	        try {
	            year = Integer.parseInt(monthYear[0]);
	            month = Integer.parseInt(monthYear[1]);
	            if (month < 1 || month > 12) {
	                System.out.println("Invalid month");
	                return;
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid year or month");
	            return;
	        }
	        
	        double totalIncome = 0;
	        double totalExpense = 0;
	        
	        for (TransactionTracker t : transactions) {
	            LocalDate date = t.getDate();
	            if (date.getYear() == year && date.getMonthValue() == month) {
	                if (t.getType() == TransactionTracker.Type.INCOME) {
	                    totalIncome += t.getAmount();
	                } else {
	                    totalExpense += t.getAmount();
	                }
	            }
	        }
	        
	        System.out.println("\n*********Summary**********");
	        System.out.println("Total Income: " + totalIncome);
	        System.out.println("Total Expense: " + totalExpense);
	        System.out.println("Net Savings: " + (totalIncome - totalExpense));
	    }




    
    

}
