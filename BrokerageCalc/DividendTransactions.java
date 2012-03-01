package actmanagement;

public class DividendTransactions extends Transaction{
	//private Stock stock;
	public boolean dividendType[] = new boolean[2];
	public static final int qualified = 0;
	public static final int ordinary = 1;
	private double amount;
	
	public DividendTransactions(int num, double amt) {
		super(num);
		//stock = st;
		if(amt < 0){
			System.out.println("Amount is less than 0");
			amount = 0.0;
		}
		else{
			amount = amt;
		}
		dividendType[qualified] = false;
		dividendType[ordinary] = false;
	}
	
	public void setDividendType(int type){
		if(type > -1 && type < 2 ){
			dividendType[type] = true;
		}
	}
	
	public String convertDivTypeToString(){
		String strtype = null;
		if(dividendType[qualified]){
			strtype = "Qualified";
		}
		else{
			strtype = "Ordinary";
		}
		return strtype;
	}
	
	public double getAmount(){
		return amount;
	}
	
	public double changeInBalance() {
		double change;
		change = amount;
		return change;
	}
	
	public void print(){
		System.out.println("\n" + "Dividend Transaction");
		System.out.println("---------------------------");
		super.print();
		System.out.println("Dividend Type: " + convertDivTypeToString());
		System.out.println("Amount: " + getAmount());
	}
	
}
