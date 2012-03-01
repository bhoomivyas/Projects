import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class WeatherUpdatePanel extends JPanel implements ActionListener{
	
	private WeatherDb weatherDb;

	private JPanel northPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	
	
	private JLabel cityLbl = new JLabel("City ");
	private JTextField cityTxtFld = new JTextField(20);
	
	private JLabel stateLbl = new JLabel("State ");
	private JTextField stateTxtFld = new JTextField(3);
	
	private JLabel tempLbl = new JLabel("Temp ");
	private JTextField tempTxtFld = new JTextField(3);
	
	private JCheckBox warningChkBx =  new JCheckBox();
	private JLabel warningLbl = new JLabel("Severe Weather Warning? ");
	
	private JButton updateBtn = new JButton("Update");
	
	
	public WeatherUpdatePanel(WeatherDb db){
		weatherDb = db;
		
		setLayout(new BorderLayout());
		northPanel.add(cityLbl);
		northPanel.add(cityTxtFld);
		
		northPanel.add(stateLbl);
		northPanel.add(stateTxtFld);
		
		centerPanel.add(tempLbl);
		centerPanel.add(tempTxtFld);
		
		centerPanel.add(warningChkBx);
		centerPanel.add(warningLbl);
		
		updateBtn.addActionListener(this);
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(updateBtn, BorderLayout.SOUTH);

	}


	@Override
	public void actionPerformed(ActionEvent event) {
		boolean warning;
		
		String city = cityTxtFld.getText();
		String state = stateTxtFld.getText();
        String str = tempTxtFld.getText();
        int temp = Integer.parseInt(str);
        
		if(state.isEmpty() || city.isEmpty()){
           	JOptionPane.showMessageDialog( this,"You must enter city and state" );
			return;
		}
		
		if(str.length()>3){
		      JOptionPane.showMessageDialog(this, "Temperature may have a maximum of three digits");
		      return;
		}
		
		if (state.length()>2){
			 JOptionPane.showMessageDialog(this, "State must be two letter abbreviation");
	         return;
		}
		
		warning = warningChkBx.isSelected();
		
		Weather weather = new Weather(city, state, temp, warning);
		if(weatherDb.hasLocation(weather.getLocation())){
			weatherDb.updateWeather(weather);
		}
		else{
			weatherDb.addWeather(weather);
		}
		
		
		cityTxtFld.setText("");
		stateTxtFld.setText("");
		tempTxtFld.setText("");
		warningChkBx.setSelected(false);
	}
	
}
