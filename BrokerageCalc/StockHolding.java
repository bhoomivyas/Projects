package actmanagement;

public class StockHolding {
	private Stock stock = new Stock();
	private int numOfSharesHeld;
	
	public void setNumOfSharesHeld(int num){
		numOfSharesHeld = num;
	}
	
	public void setStockName(String name){
		stock.setStockName(name);
	}
	
	public void setStockSymbol(String symbol){
		stock.setStockSymbol(symbol);
	}
	
	public void setClosingPrice(double price){
		stock.setClosingPrice(price);
	}
	
	public String getStockName(){
		return stock.getStockName();
	}
	
	public String getStockSymbol(){
		return stock.getStockSymbol();
	}
	
	public double getClosingPrice(){
		return stock.getClosingPrice();
	}
	
	public int getNumOfSharesHeld(){
		return numOfSharesHeld;
	}
	
	
}
