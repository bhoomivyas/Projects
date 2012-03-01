package actmanagement;

public class Stock {
	private String stockName;
	private String stockSymbol;
	private double closingPrice;
	
	public void setStockName(String name){
		stockName = name;
	}
	
	public void setStockSymbol(String symbol){
		stockSymbol = symbol;
	}
	
	public void setClosingPrice(double price){
		closingPrice = price;
	}
	
	public String getStockName(){
		return stockName;
	}
	
	public String getStockSymbol(){
		return stockSymbol;
	}
	
	public double getClosingPrice(){
		return closingPrice;
	}

}
