import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import orderdata.Customer;

public class CustomerPanel extends JPanel {
	private JLabel phoneLbl;
	private JTextField phoneTextField;
	private JLabel nameLbl;
	private JTextField nameTextField;
	
	private ArrayList<Customer> list;
	private Customer cusobj;
	
	public CustomerPanel(ArrayList<Customer> cusList){
		list = cusList;
		
		phoneLbl = new JLabel("Phone: ");
		add(phoneLbl);
		
		phoneTextField = new JTextField(12);
		phoneTextField.addActionListener(new PhoneTxtHandler());
		add(phoneTextField);
		
		nameLbl = new JLabel("Name: ");
		add(nameLbl);
		
		nameTextField = new JTextField(18);
		nameTextField.setEditable(false);
		nameTextField.setBackground(Color.LIGHT_GRAY);
		add(nameTextField);
	}
	
	public void reset() {
		phoneTextField.setText("");
		nameTextField.setText("");
	}
	
	
	class PhoneTxtHandler implements ActionListener{
		public void actionPerformed(ActionEvent event){
			int i;
			boolean flag = false;
			for(i=0; i<list.size(); i++){
				if(phoneTextField.getText().equals(list.get(i).getPhoneNum())){
					nameTextField.setText(list.get(i).getCustName());
					cusobj = list.get(i);

					flag = true;
					break;
				}
			}
			if(!flag){
					JOptionPane.showMessageDialog(phoneTextField, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	public Customer getCusObj(){
		return cusobj;
	}
	
}
