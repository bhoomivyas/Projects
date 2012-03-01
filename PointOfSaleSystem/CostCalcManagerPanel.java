import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import orderdata.*;
import java.util.*;

public class CostCalcManagerPanel extends JPanel{
	private JPanel costCalcPanel;
	private CustomerPanel cusPanel;
	private OrderDetailsPanel ordPanel;
	private ToppingsPanel topPanel;
	
	private JLabel costLbl;
	private JTextField costTxtFld;
	private JButton totalBtn;
	private JButton enterBtn;
	
	private ArrayList<Customer> list;
	private PizzaOrder pizzaorder;
	private GregorianCalendar date;
	
	public CostCalcManagerPanel(){
		costCalcPanel = new JPanel();
		
		list = createCusList();
		cusPanel = new CustomerPanel(list);
		
		date = new GregorianCalendar();
		pizzaorder = new PizzaOrder(cusPanel.getCusObj(), date);
		
		ordPanel = new OrderDetailsPanel(pizzaorder);
		pizzaorder = ordPanel.getPizzaOrdObj();
		
		topPanel = new ToppingsPanel(pizzaorder);
		pizzaorder = topPanel.getPizzaObj();
		
		costLbl = new JLabel("Cost: ");
		costCalcPanel.add(costLbl);
		
		
		costTxtFld = new JTextField(6);
		costTxtFld.setEditable(false);
		costTxtFld.setBackground(Color.LIGHT_GRAY);
		costCalcPanel.add(costTxtFld);
		
		totalBtn = new JButton("Total");
		totalBtn.addActionListener(new TotalBtnHandler());
		costCalcPanel.add(totalBtn);
		
		enterBtn = new JButton("Enter");
		enterBtn.addActionListener(new EnterBtnHandler());
		costCalcPanel.add(enterBtn);
		
		setLayout(new GridLayout(4,0));
		add(cusPanel);
		add(ordPanel);
		add(topPanel);
		add(costCalcPanel);
		
	}
	
	public void reset(){
		costTxtFld.setText("");
	}
	
	public ArrayList<Customer> createCusList(){
		Customer cus, cus1, cus2;
		ArrayList<Customer> cusList = new ArrayList<Customer>();
		
		cus = new Customer("John Doe");
		cus.setPhoneNum("408-123-4567");
		cusList.add(cus);
		
		cus1 = new Customer("Jane Doe");
		cus1.setPhoneNum("111-111-1111");
		cusList.add(cus1);
		
		cus2 = new Customer("Mary");
		cus2.setPhoneNum("222-222-2222");
		cusList.add(cus2);
		
		return cusList;
		
	}
	class TotalBtnHandler implements ActionListener{
		public void actionPerformed(ActionEvent event){
	
			double total;
			total = pizzaorder.costCal();
			String cost;
			cost = Double.toString(total);
			
			costTxtFld.setText(cost);
			
		}
	}
	
	class EnterBtnHandler implements ActionListener{
		public void actionPerformed(ActionEvent event){
			cusPanel.reset();
			topPanel.reset();
			reset();
			Customer customer;
			customer = cusPanel.getCusObj();
			System.out.println("Customer Name: " + customer.getCustName());
			System.out.println("Customer Number: " + customer.getPhoneNum());
			
			String orders;
			orders = pizzaorder.toString();
			System.out.println(orders);
		}
	}
}
