package actmanagement;

public class CashTransaction extends Transaction{
	public boolean cashTransType[] = new boolean[2];
	public static final int deposit = 0;
	public static final int withdrawal = 1;
	public boolean routingNumber[] = new boolean[3];
	public static final int BofA = 0;
	public static final int Chase = 1;
	public static final int ICICI = 2;
	private double amount;
	
	public CashTransaction(int num, double amt) {
		super(num);
		if(amt<0){
			System.out.println("Amount is less than 0");
			amount = 0.0;
		}
		else{
			amount = amt;
		}
		cashTransType[deposit] = false;
		cashTransType[withdrawal] = false;
		routingNumber[BofA] = false;
		routingNumber[Chase] = false;
		routingNumber[ICICI] = false;
	}
	
	public void setCashTransType(int type){
		if( type > -1 && type < 2){
			cashTransType[type] = true;
		}
	}
	
	public void setRoutingNumber(int routingnum){
		if( routingnum > -1 && routingnum < 3)
		{
			routingNumber[routingnum] = true;
		}
	}
	
	public String convertCTransToString(){
		String strType = null;
		if(cashTransType[deposit])
		{
			strType = "Deposit";
		}
		else{
			strType = "Withdrawal";
		}
		return strType;
	}

	public String convertRoutNumToBankName(){
		String strBankName = null;
		if(routingNumber[BofA]){
			strBankName = "Bank Of America";
		}
		else if(routingNumber[Chase]){
			strBankName = "Chase";
		}
		else{
			strBankName = "ICICI";
		}
		return strBankName;
	}
	
	public double getAmount(){
		return amount;
	}
	
	public double changeInBalance() {
		double change=0;
		if(cashTransType[deposit]){
			change = amount;
		}
		else{
			change = amount * -1;
		}
		return change;
	}
	
	public void print(){
		System.out.println("\n" + "Cash Transaction");
		System.out.println("-----------------");
		super.print();
		System.out.println("Transaction type: " + convertCTransToString());
		System.out.println("Bank Name: " + convertRoutNumToBankName());
		System.out.println("Amount: " + getAmount());
	}
}
