package com.expensetracker;

import java.time.LocalDate;

public class TransactionTracker {
	
	    public enum Type { INCOME, EXPENSE }

	    private Type type;
	    private String category;
	    private double amount;
	    private LocalDate date;


		public TransactionTracker(Type type, String category, double amount, LocalDate date) {
	        this.type = type;
	        this.category = category;
	        this.amount = amount;
	        this.date = date;
	    }

	    public Type getType() {
	        return type;
	    }

	    public String getCategory() {
	        return category;
	    }

	    public double getAmount() {
	        return amount;
	    }

	    public LocalDate getDate() {
	        return date;
	    }

	    @Override
	    public String toString() {
	        return type + " | " + category + " | " + amount + " | " + date;
	    }
}
