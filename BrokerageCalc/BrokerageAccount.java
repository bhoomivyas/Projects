package actmanagement;
import java.util.*;

public class BrokerageAccount {
	private int accountNumber;
	private String clientName;
	private double cashBalance;
	private ArrayList<StockHolding> listOfSharesHeld;
	
	public BrokerageAccount(ArrayList<StockHolding> stockhold,int accountnum){
		listOfSharesHeld = stockhold;
		accountNumber = accountnum;
		cashBalance = 0.0;
	}
	
	public void setClientName(String name){
		clientName = name;
	}
	
	public int getAccountNumber(){
		return accountNumber;
	}
	
	public String getClientName(){
		return clientName;
	}
	
	public double getCashBalance(){
		return cashBalance;
	}
	
	public void applyTransaction(Transaction trans){
		
		cashBalance = cashBalance + trans.changeInBalance();
		
		int numofshares = ((StockTransaction)trans).getNumOfShares();
		//String stocksymbol = ((StockTransaction)trans).getStockSymbol();
		String symbol = ((StockTransaction)trans).getStockSymbol();
		double price = ((StockTransaction)trans).getClosingPrice();
		
		StockHolding stockhold = null;
		StockHolding stockhold2 = new StockHolding();
		
		String stocksymbol1 = null;
		boolean foundSymbol = false;
		
		if(trans instanceof StockTransaction){
			for(int i=0; i<listOfSharesHeld.size(); i++){
				stockhold = listOfSharesHeld.get(i);
				stocksymbol1 = stockhold.getStockSymbol();
				
				if(stocksymbol1.equals(symbol)){
					foundSymbol = true;
					//((StockHolding)stockhold).setNumOfSharesHeld(numofshares);
					break;
				}
			}
			
			if(foundSymbol){
				((StockHolding)stockhold).setNumOfSharesHeld(numofshares);
			}
			else{
					//((StockHolding)stockhold).setStockName(stocksymbol1);
				((StockHolding)stockhold2).setStockSymbol(symbol);
				((StockHolding)stockhold2).setClosingPrice(price);
				((StockHolding)stockhold2).setNumOfSharesHeld(numofshares);
			}
		
			if(numofshares != 0){	
				listOfSharesHeld.add(stockhold);
			}
			else{
				listOfSharesHeld.remove(stockhold);
			}
			
			cashBalance = cashBalance + trans.changeInBalance();

		}
	}
	
	
	public void applyTransactions(Transaction trans[], int numTrans){
		int i;
		for(i=0; i<numTrans; i++){
			cashBalance = cashBalance + trans[i].changeInBalance();
		}
	}
	
	public double accountValue(){
		int i;
		double sum =0;
		double closeprice =0;
		double value = 0;
		int numofshares = 0;
		StockHolding stockhold;
		for(i=0; i<listOfSharesHeld.size(); i++){
			stockhold = listOfSharesHeld.get(i);
			numofshares = stockhold.getNumOfSharesHeld();
			closeprice = stockhold.getClosingPrice();
			value = numofshares * closeprice;
			sum = sum + value;
		}
		return sum;
	}
	
	public double totalAccountValue(){
		return getCashBalance() + accountValue();
	}
	
	public void print(){
		double total =0;
		
		System.out.println("\n" + "Brokerage Account");
		System.out.println("----------------------");
		System.out.println("Name: " + getClientName());
		System.out.println("Account Number: " + getAccountNumber());
		System.out.println("Cash Balance: " + getCashBalance());
		System.out.println("Account Value: " + accountValue());
		
		total = totalAccountValue();
		System.out.println("Total Account Value: " + total);
	}
}
