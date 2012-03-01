import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
//import java.util.*;

public class WeatherTablePanel extends JPanel {
	public final String cols[] = { "City", "State", "Temp", "Max Temp", "Min Temp", "Warning"};
	
	private JPanel westPanel = new JPanel();
	private JPanel southPanel = new JPanel();
	private JPanel eastPanel = new JPanel();
	
	private JLabel cityLbl = new JLabel("City");
	private JTextField cityTxtFld = new JTextField(20);
	
	private JLabel stateLbl = new JLabel("State");
	private JTextField stateTxtFld = new JTextField(3);
	
	private JButton addBtn = new JButton("Add");
	private JButton removeBtn = new JButton("Remove Location");
	
	private WeatherDb weatherDb;
	private Location loc;
	
	private TableModel tblModel;
	private JTable weatherTbl;
	private Dimension d;
	
	public WeatherTablePanel(WeatherDb db){
		weatherDb = db;
		tblModel = new WeatherTableModel(weatherDb);
		weatherTbl = new JTable(tblModel);
		d = new Dimension(410,140);
		
		weatherTbl.setDefaultRenderer(Component.class, new MyCellRenderer());
		
		weatherTbl.setPreferredScrollableViewportSize(d);
		
		
		JScrollPane scroll = new JScrollPane(weatherTbl);
		westPanel.add(scroll);
		
		removeBtn.addActionListener(new RemoveBtnHandler());
		eastPanel.add(removeBtn);
		
		southPanel.add(cityLbl);
		southPanel.add(cityTxtFld);
		southPanel.add(stateLbl);
		southPanel.add(stateTxtFld);
		
		addBtn.addActionListener(new AddBtnHandler());
		southPanel.add(addBtn);
		
		setLayout(new BorderLayout());
		add(westPanel, BorderLayout.WEST);
		add(eastPanel, BorderLayout.EAST);
		add(southPanel, BorderLayout.SOUTH);
		
	}

    class AddBtnHandler implements ActionListener {      
        public void actionPerformed(ActionEvent event) {
        	
        	String cityName  = cityTxtFld .getText();
            String stateName = stateTxtFld.getText();
            
            try{
    	    	int found = 0;
    	    	loc = new Location(cityName, stateName);
    	        found = ((WeatherTableModel)tblModel).addRecord(loc);
    	        
    	        if(found==0){
    	        	JOptionPane.showMessageDialog(cityTxtFld, "City & State combination does not exist!!");
    	        }
    	        else if(found==2){
    	        	JOptionPane.showMessageDialog(cityTxtFld, "City & State combination already added in table!!");
    	        }
    	        cityTxtFld.setText("");
    	        stateTxtFld.setText("");
    	        
        	}catch(Exception e){
            	JOptionPane.showMessageDialog(cityTxtFld, e.getMessage());
            }
            
        }
 
     }
    
	
    class RemoveBtnHandler implements ActionListener {      
        public void actionPerformed(ActionEvent event) {
        	try{
	        	int index = weatherTbl.getSelectedRow();
        		((WeatherTableModel)tblModel).removeRecord(weatherTbl.convertRowIndexToModel(index));
	        	weatherTbl.clearSelection();
        	}catch(Exception e1){
            	JOptionPane.showMessageDialog(weatherTbl,  "No rows selected");
            }
          }
        	
     }
    
    class MyCellRenderer implements TableCellRenderer {      
    	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row,int column)      
    	{      
    		Component com = (Component)value;      
    		return com;      
    	}
    }

}


