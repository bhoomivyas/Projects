package actmanagement;
import java.util.*;

abstract public class Transaction implements Comparable<Transaction>{
	private int transactionNum;
	private double amount;
	private GregorianCalendar date;

	
	public Transaction(int num){
		transactionNum = num;
	}
	
	public void setTransactionDate(GregorianCalendar gc){
		date = gc;
	}
	
	public int getTransactionNum(){
		return transactionNum;
	}
	public double getTransactionAmount(){
		return amount;
	}
	
	public GregorianCalendar getTransactionDate(){
		return date;
	}
	
	public int compareTo(Transaction other){
		if(amount < other.amount)
		{
			return -1;
		}
		else if(amount > other.amount){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	public void print(){
		//String month = String.valueOf(date.get(GregorianCalendar.MONTH));
		//String year = String.valueOf(date.get(GregorianCalendar.YEAR));
		//String day = String.valueOf(date.get(GregorianCalendar.DAY_OF_MONTH));
		//System.out.println("Date Of Transaction: " + month + "/" + day + "/" + year );
		System.out.println("Transaction Number: " + getTransactionNum());
	}
	
	abstract public double changeInBalance();

}
