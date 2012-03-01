import javax.swing.*;
import java.awt.event.*;
import orderdata.*;


public class ToppingsPanel extends JPanel {
	private JLabel toppingLbl;
	private JCheckBox pepchkBox;
	private JCheckBox sauschkBox;
	private JCheckBox onionchkBox;
	private JCheckBox tomatochkBox;

	private PizzaOrder pizzaorder;
	
	
	public ToppingsPanel(PizzaOrder pz){
		pizzaorder = pz;
		
		toppingLbl = new JLabel("Toppings: ");
		add(toppingLbl);
		
		pepchkBox = new JCheckBox("Pepperoni");
		pepchkBox.addItemListener(new CheckBoxHandler());
		add(pepchkBox);
		
		sauschkBox = new JCheckBox("Sausage");
		sauschkBox.addItemListener(new CheckBoxHandler());
		add(sauschkBox);
		
		onionchkBox = new JCheckBox("Onion");
		onionchkBox.addItemListener(new CheckBoxHandler());
		add(onionchkBox);
		
		tomatochkBox = new JCheckBox("Tomato");
		tomatochkBox.addItemListener(new CheckBoxHandler());
		add(tomatochkBox);
		
	}
	
	class CheckBoxHandler implements ItemListener{
		public void itemStateChanged(ItemEvent event){
			
			StringBuilder result = new StringBuilder();
			result.append("Toppings: ");
			if(pepchkBox.isFocusOwner()){
				result.append("Pepperoni" + " ");
				pizzaorder.addTopping(PizzaOrder.pepperoni);
			}
			if(sauschkBox.isFocusOwner()){
				result.append("Sausage" + " ");
				pizzaorder.addTopping(PizzaOrder.sausage);
			}
			if(onionchkBox.isFocusOwner()){
				result.append("Onion" + " ");
				pizzaorder.addTopping(PizzaOrder.onions);
			}
			if(tomatochkBox.isFocusOwner()){
				result.append("Tomato" + " ");
				pizzaorder.addTopping(PizzaOrder.tomatoes);
			}
			//System.out.println(result.toString());
		}
	}
	
	public void reset(){
		pepchkBox.setSelected(false);
		sauschkBox.setSelected(false);
		onionchkBox.setSelected(false);
		tomatochkBox.setSelected(false);
	}
	
	public PizzaOrder getPizzaObj(){
		return pizzaorder;
	}
	
}
