import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.*;

import javax.swing.JCheckBox;

public class WeatherTableModel extends AbstractTableModel implements Observer{

	private final String colNames[] = { "City", "State", "Temp", "Max Temp", "Min Temp", "Warning"};
	private JCheckBox warningChkBx = new JCheckBox();
	private WeatherDb weatherDb; 
	private ArrayList<Weather> weatherList = new ArrayList<Weather>(10);
	
	public WeatherTableModel(WeatherDb db){
		weatherDb = db;
		weatherDb.addObserver(this);
	}
	
	@Override
	public int getRowCount() {
		return weatherList.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	public void setValueAt(Object value, int row, int col){
		if(col == 5){
			if(value.equals("true")){
				warningChkBx.setSelected(true);
			}
		}
	}
	
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Weather weather = weatherList.get(rowIndex);
		
		if(columnIndex == 0){
			return weather.getLocation().getCity();
			
		}else if(columnIndex == 1){
			return weather.getLocation().getState();
			
		}else if(columnIndex == 2){
			return weather.getTemp();
			
		}else if(columnIndex == 3){
			return weather.getMaxTemp();
			
		}else if(columnIndex == 4){
			return weather.getMinTemp();
			
		}else if(columnIndex == 5){
			return weather.getWeatherWarning();
			
		}else{
			return null;
		}
		
	}

	
	public String getColumnName(int Index){
		return colNames[Index];
	}

	public int addRecord(Location loc){
		Weather newWeather;
		int found = 0;
		boolean flag = false;
		
		for(int i=0; i<weatherList.size(); i++){
			newWeather = weatherList.get(i);
			System.out.println(newWeather.getLocation().getCity());
			if(newWeather.getLocation().equals(loc)){
				flag = true;
				found = 2;
				break;
			}
		}
		
		if(!flag){
			if(weatherDb.hasLocation(loc)){
				weatherList.add(weatherDb.getWeather(loc));
				flag = true;
				found = 1;
				fireTableDataChanged();
			}
		}
		
		return found;
	}
	
	public void updateWeatherList(Object newWeather){
		Weather weather = (Weather) newWeather;
		Weather tempWeather;
		
		for(int i=0; i<weatherList.size(); i++){
			tempWeather = weatherList.get(i);
			if(tempWeather.equals(weather)){
				System.out.println(tempWeather.getLocation().getCity());
				tempWeather.setTemp(weather.getTemp());
				tempWeather.setMaxTemp(weather.getMaxTemp());
				tempWeather.setMinTemp(weather.getMinTemp());
				tempWeather.setWeatherWarning(weather.getWeatherWarning());
				fireTableDataChanged();
			}
		}
	}
	
	public void removeRecord(int index){
		weatherList.remove(index);
		fireTableDataChanged();
	}
	
	@Override
	public void update(Observable observableObj, Object newWeatherLoc) {
		if(observableObj==weatherDb){
			updateWeatherList(newWeatherLoc);
		}
	}
	

	public Class getColumnClass(int column) {         
		return getValueAt(0, column).getClass();         
	} 
	
	
}
