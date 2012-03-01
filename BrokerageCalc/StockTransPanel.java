import java.awt.*;

import javax.swing.*;

import java.util.*;
import java.awt.event.*;
import actmanagement.*;

public class StockTransPanel extends JPanel{
	private JPanel northPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	private JPanel southPanel = new JPanel();
	
	private JLabel transIdLbl;
	private JTextField idTxtFld;
	private JLabel dateLbl;
	private JTextField dateTxtFld;
	
	private JComboBox transBox;
	private JLabel symbolLbl;
	private JTextField symbolTxtFld;
	private JLabel sharesLbl;
	private JTextField sharesTxtFld;
	private JLabel priceLbl;
	private JTextField priceTxtFld;
	
	private JButton enterBtn;
	
	private GregorianCalendar date;
	private BrokerageAccount account;
	private Stock knownStocks[];
	private StockTransaction strans;
	
	public int idNum;
	
	public StockTransPanel(BrokerageAccount act){
		account = act;
		knownStocks = getArrayOfStocks();
		
		transIdLbl = new JLabel("Transaction Id: ");
		northPanel.add(transIdLbl);
		
		idTxtFld = new JTextField(6);
		idTxtFld.addActionListener(new IdTxtFldHandler());
		northPanel.add(idTxtFld);
		
		dateLbl = new JLabel("    Date");
		northPanel.add(dateLbl);	
	
		dateTxtFld = new JTextField(8);
		dateTxtFld.setForeground(Color.RED);
		Font f = new Font(dateTxtFld.getName(),Font.BOLD, dateTxtFld.getFont().getSize());
		dateTxtFld.setFont(f);
		northPanel.add(dateTxtFld);
		
		transBox = new JComboBox();
		transBox.addItem("Buy");
		transBox.addItem("Sell");
		centerPanel.add(transBox);
		
		symbolLbl = new JLabel("    Symbol:  ");
		centerPanel.add(symbolLbl);
		
		symbolTxtFld = new JTextField(5);
		centerPanel.add(symbolTxtFld);
		
		sharesLbl = new JLabel("   Num Shares:  ");
		centerPanel.add(sharesLbl);
		
		sharesTxtFld = new JTextField(5);
		centerPanel.add(sharesTxtFld);
		
		priceLbl = new JLabel("  Price:   $");
		centerPanel.add(priceLbl);
		
		priceTxtFld = new JTextField(7);
		centerPanel.add(priceTxtFld);
		
		enterBtn = new JButton("Enter Transaction");
		enterBtn.addActionListener(new EnterBtnHandler());
		southPanel.add(enterBtn);
		
		setLayout(new GridLayout(3,0));
		add(northPanel);
		add(centerPanel);
		add(southPanel);
		
	}
	
	public void reset(){
		idTxtFld.setText("");
		dateTxtFld.setText("");
		symbolTxtFld.setText("");
		sharesTxtFld.setText("");
		priceTxtFld.setText("");
	}
	
	public Stock[] getArrayOfStocks(){
		Stock knownStocks[] = new Stock[3];
		
		knownStocks[0] = new Stock();
		knownStocks[0].setStockName("Google");
		knownStocks[0].setStockSymbol("GOOG");
		knownStocks[0].setClosingPrice(215.10);
		
		knownStocks[1] = new Stock();
		knownStocks[1].setStockName("Yahoo");
		knownStocks[1].setStockSymbol("YHOO");
		knownStocks[1].setClosingPrice(17.15);
		
		knownStocks[2] = new Stock();
		knownStocks[2].setStockName("Microsoft");
		knownStocks[2].setStockSymbol("MSFT");
		knownStocks[2].setClosingPrice(25);
		
		return knownStocks;
	}
	
	public ArrayList<StockHolding> getListOfStocksHeld(){
		ArrayList<StockHolding> stocklist = new ArrayList<StockHolding>(2);
		StockHolding sh1, sh2;
		
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
		
		return stocklist;
	}
	
	class IdTxtFldHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			try{
				idNum = Integer.parseInt(idTxtFld.getText());
				if(idNum <= 100){
					throw new BadTransactionEx("Please enter ID greater than 100");
				}
				else{
					strans = new StockTransaction(idNum);
					StringBuilder result = new StringBuilder();
					date = new GregorianCalendar();		
					String month = String.valueOf(date.get(GregorianCalendar.MONTH));
					String year = String.valueOf(date.get(GregorianCalendar.YEAR));
					String day = String.valueOf(date.get(GregorianCalendar.DAY_OF_MONTH));
					
					result.append(month + "/" + day + "/" + year);
					
					dateTxtFld.setText(result.toString());
				}
			}catch(NumberFormatException ex){
				JOptionPane.showMessageDialog(null, "Please enter a valid Transaction ID");
			}catch(BadTransactionEx ex){
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
	}
	
	
	class EnterBtnHandler implements ActionListener{
		public void actionPerformed(ActionEvent event){
			
			boolean foundStock = false;
			
			try{
				
				for(int i=0; i<knownStocks.length; i++){
					if(symbolTxtFld.getText().equals(knownStocks[i].getStockSymbol())){
						foundStock = true;
						break;
					}
				}
			
				if(foundStock == true){
				 idNum = Integer.parseInt(idTxtFld.getText());
				 strans = new StockTransaction(idNum);
				
					if(transBox.getSelectedItem().equals("Sell")){
						strans.setStockTransType(StockTransaction.sold);
					}
					else{
						strans.setStockTransType(StockTransaction.bought);
					}
					
					strans.setStockSymbol(symbolTxtFld.getText());
					
					if(sharesTxtFld.getText().equals("0") || sharesTxtFld.getText().equals("")){
						throw new BadTransactionEx("Please enter a valid Number of Shares");
					}
					else{
						int num = Integer.parseInt(sharesTxtFld.getText());
						strans.setNumOfShares(num);
					}
					
					if(priceTxtFld.getText().equals("0") || priceTxtFld.getText().equals("")){
						throw new BadTransactionEx("Please enter a valid Price");
					}
					else{
						double price = Double.parseDouble(priceTxtFld.getText());
						strans.setPriceOfStock(price);
					}
					
					strans.calculateAmount();
				
					strans.calculateCommission();
				
					strans.print();
				
					account.applyTransaction(strans);
				
					reset();
				}
				else{
					throw new BadTransactionEx("Please enter a valid Stock Symbol");
				}
			}catch(BadTransactionEx ex){
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}catch(NumberFormatException ex){
				JOptionPane.showMessageDialog(null, "Please enter integer instead of characters");
			}
		}
	}
}
