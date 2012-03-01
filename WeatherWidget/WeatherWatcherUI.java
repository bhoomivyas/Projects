import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class WeatherWatcherUI extends JPanel{
	private JPanel watchPanel = new JPanel(new GridLayout(2,0));
	private JButton tblBtn = new JButton("New Weather Table");
	private JButton setBtn = new JButton("New Weather Setter");
	
	private WeatherDb weatherDb = new WeatherDb();
	
	public WeatherWatcherUI(){
		tblBtn.addActionListener(new WeatherTableHandler());
		watchPanel.add(tblBtn);
		
		setBtn.addActionListener(new WeatherUpdateHandler());
		watchPanel.add(setBtn);
		
		add(watchPanel);
	}
	
	class WeatherUpdateHandler implements ActionListener {      
    	public void actionPerformed(ActionEvent evt) {
    		WeatherUpdatePanel weatherSetter = new WeatherUpdatePanel(weatherDb);              
		   	WeatherUpdateFrame frame = new WeatherUpdateFrame(weatherSetter);
		   	frame.setTitle("Weather Update");
		   	frame.setSize(400, 150);
		    frame.setVisible(true);
    	}
	}
	
	class WeatherTableHandler implements ActionListener {      
    	public void actionPerformed(ActionEvent evt) {
    		WeatherTablePanel weatherTable = new WeatherTablePanel(weatherDb);              
		   	WeatherTableFrame frame = new WeatherTableFrame(weatherTable);
		   	frame.setTitle("Weather Table");
		    frame.pack();
		    frame.setVisible(true);
	   }           
   }
	
	public static void main(String[] args) {
		
		 WeatherWatcherUI watchPanel = new WeatherWatcherUI();
		 WeatherUpdateFrame frame = new WeatherUpdateFrame(watchPanel);
		 frame.setTitle("Weather Watcher Manager");
		 frame.pack();
		 frame.setVisible(true);
	}
	
}


class WeatherTableFrame extends JFrame{
	public WeatherTableFrame(JPanel topPanel) {
	 	   setDefaultCloseOperation(EXIT_ON_CLOSE);
	       add(topPanel);
	 }
}


class WeatherUpdateFrame extends JFrame{
	public WeatherUpdateFrame(JPanel topPanel){
		Container top = getContentPane();
	 	   setDefaultCloseOperation(EXIT_ON_CLOSE);
	       top.add(topPanel);
	}
}
