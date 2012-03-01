import javax.swing.*;

public class PizzaGui {
	public static void main(String args[]){
		CostCalcManagerPanel costCalcPanel = new CostCalcManagerPanel();
		
		JFrame frame = new JFrame();
		frame.setSize(470, 210);
		frame.setLocation(400, 250);
		frame.setTitle("Pizza Order Entry");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(costCalcPanel);
		frame.setVisible(true);
	}
	
}

