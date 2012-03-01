import java.util.ArrayList;
import java.io.*;
import javax.swing.*;
import actmanagement.*;
import java.awt.event.*;

public class StockGui {
	public static JFrame frame;
	public static BrokerageAccount acc;
	
	public static void main(String args[]){
		
		frame = new JFrame();
		
		acc = new BrokerageAccount(getListOfStocksHeld(), 1111);
		
		
		StockTransPanel stPanel = new StockTransPanel(acc);
		StockGui stk = new StockGui();
		
		frame.setSize(510, 200);
		frame.setLocation(400, 250);
		frame.setTitle("Enter Stock Transactions");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.add(stPanel);
		frame.setVisible(true);
		frame.addWindowListener(stk.new FrameTerminator());
	}
	
	public static ArrayList<StockHolding> getListOfStocksHeld(){
		ArrayList<StockHolding> stocklist = new ArrayList<StockHolding>(3);
		StockHolding sh1, sh2, sh3;
		
		sh1 = new StockHolding();
		sh1.setStockName("Google");
		sh1.setStockSymbol("GOOG");
		sh1.setClosingPrice(110.10);
		sh1.setNumOfSharesHeld(20);
		stocklist.add(sh1);
		
		sh2 = new StockHolding();
		sh2.setStockName("Microsoft");
		sh2.setStockSymbol("MSFT");
		sh2.setClosingPrice(25.05);
		sh2.setNumOfSharesHeld(10);
		stocklist.add(sh2);
		
		sh3 = new StockHolding();
		sh3.setStockName("Yahoo");
		sh3.setStockSymbol("YHOO");
		sh3.setClosingPrice(16.05);
		sh3.setNumOfSharesHeld(30);
		stocklist.add(sh3);
		
		return stocklist;
	}
	
	class FrameTerminator implements WindowListener{
		public void windowClosing(WindowEvent event){
			int response = JOptionPane.showConfirmDialog(frame, "Exit the Application?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			
			if (response == JOptionPane.YES_OPTION) {
				try{
					FileOutputStream fileOut = new FileOutputStream("transaction.dat");
					BufferedOutputStream bufOut = new BufferedOutputStream(fileOut);
					DataOutputStream outStream = new DataOutputStream(bufOut);
				
					outStream.writeInt(acc.getAccountNumber());
					outStream.writeDouble(acc.getCashBalance());
					outStream.writeDouble(acc.accountValue());
					outStream.writeDouble(acc.totalAccountValue());
					outStream.close();
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}
				
				System.exit(0);
			}
			
		}
		
		public void windowActivated(WindowEvent evt) { } 
		public void windowClosed(WindowEvent evt) { } 
		public void windowDeactivated(WindowEvent evt) { } 
		public void windowDeiconified(WindowEvent evt) { } 
		public void windowIconified(WindowEvent evt) { } 
		public void windowOpened(WindowEvent evt) { }
	}
	
}


