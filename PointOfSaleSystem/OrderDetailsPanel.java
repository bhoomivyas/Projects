import javax.swing.*;
import java.awt.event.*;

import orderdata.*;

public class OrderDetailsPanel extends JPanel{
	private JComboBox orderType;
	private JLabel sizeLbl;
	private JComboBox pizzaSize;
	
	private PizzaOrder pizzaorder;
	
	public OrderDetailsPanel(PizzaOrder pz){
		pizzaorder = pz;
		
		orderType = new JComboBox();
		sizeLbl = new JLabel("Size: ");
		pizzaSize = new JComboBox();
		
	
		orderType.addItem("Pickup");
		orderType.addItem("Delivery");
		orderType.setEditable(false);
		orderType.setSelectedIndex(0);
		pizzaorder.setOrderType(orderType.getSelectedItem().toString());
		orderType.addActionListener(new ComboBoxHandler());
		add(orderType);
		
		add(sizeLbl);
		
		pizzaSize.addItem("Small");
		pizzaSize.addItem("Medium");
		pizzaSize.addItem("Large");
		pizzaSize.setEditable(false);
		pizzaSize.setSelectedIndex(0);
		pizzaorder.setPizzaSize(pizzaSize.getSelectedItem().toString());
		pizzaSize.addActionListener(new ComboBoxHandler());
		add(pizzaSize);
		
	}
	
	class ComboBoxHandler implements ActionListener{
		public void actionPerformed(ActionEvent event){
				pizzaorder.setOrderType(orderType.getSelectedItem().toString());
			
				pizzaorder.setPizzaSize(pizzaSize.getSelectedItem().toString());
			
		}
	}
	
	
	public PizzaOrder getPizzaOrdObj(){
		return pizzaorder;
	}
	
	
	
}
