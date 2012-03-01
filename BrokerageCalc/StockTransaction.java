package actmanagement;

public class StockTransaction extends Transaction{
	private Stock stock = new Stock();
	private double priceOfStock;
	private int numOfShares;
	public boolean stockTransType[] = new boolean[2];
	public static final int bought = 0;
	public static final int sold = 1;
	private double amount;
	private double commissionCost;
	
		
	public StockTransaction(int num){
		super(num);
		amount =0;
		commissionCost = 0;
		stockTransType[bought] = false;
		stockTransType[sold] = false;
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
	
	public void setPriceOfStock(double price){
		priceOfStock = price;
	}
	
	public void setNumOfShares(int shares){
		numOfShares = shares;
	}
	
	public void setStockTransType(int stype){
		if(stype > -1 && stype < 2){
			stockTransType[stype] = true;
		}
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
	
	public double getPriceOfStock(){
		return priceOfStock;
	}
	
	public int getNumOfShares(){
		return numOfShares;
	}
	
	public String convertSTransToString(){
		String strType = null;
		if(stockTransType[bought]){
			strType = "Buy";
		}
		else{
			strType = "Sell";
		}
		return strType;
	}
	
	public void calculateAmount(){
		if(stockTransType[bought]){
			amount = numOfShares * priceOfStock + commissionCost;
		}
		else{
			amount = numOfShares * priceOfStock - commissionCost;
			if(amount < 0){
				System.out.println("Amount is less than 0");
				amount = 0.0;
			}
		}
	}
	
	public double getAmount(){
		return amount;
	}
	
	public void calculateCommission(){
		if(numOfShares >= 1000){
			commissionCost = numOfShares/100;
		}
		else{
			commissionCost = 10.00;
		}
	}
	
	public double getCommissionCost(){
		return commissionCost;
	}
	
	public double changeInBalance() {
		double change=0;
		if(stockTransType[bought]){
			change = amount * -1;
		}
		else{
			change = amount;
		}
		return change;
	}
	
	public void print(){
		System.out.println("Stock Transaction");
		System.out.println("------------------");
		super.print();
		//System.out.println("Stock Name: " + getStockName());
		System.out.println("Stock Symbol: " + getStockSymbol());
		//System.out.println("Closing Price: " + getClosingPrice());
		System.out.println("Price Of Stock: " + getPriceOfStock());
		System.out.println("Number Of Shares: " + getNumOfShares());
		System.out.println("Transaction type: " + convertSTransToString());
		System.out.println("Commission: " + getCommissionCost());
		System.out.println("Amount: " + getAmount());
	}
}
