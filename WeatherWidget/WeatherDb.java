import java.util.*;

public class WeatherDb extends Observable{

	private HashMap<Location, Weather> weatherMap = new HashMap<Location, Weather>();
	Location loc;
	
	public void addWeather(Weather weather) {
		loc = weather.getLocation();
		weatherMap.put(loc, weather);
	}

	public Weather getWeather(Location loc){
		Weather weather;
		weather = (Weather) weatherMap.get(loc);
		return weather;
	}
	
	public Weather updateWeather(Weather weather){
		Location loc = weather.getLocation();
		Weather weatherLoc = weatherMap.get(loc);
		
		if(weatherLoc.getLocation().equals(weather.getLocation())){
			
			weatherLoc.setTemp(weather.getTemp());
			
			if(weather.getTemp()>weatherLoc.getMaxTemp()){
				weatherLoc.setMaxTemp(weather.getTemp());
			}
			
			if(weather.getTemp()<weatherLoc.getMinTemp()){
				weatherLoc.setMinTemp(weather.getTemp());
			}
			
			weatherLoc.setWeatherWarning(weather.getWeatherWarning());
			setChanged();
			notifyObservers(weatherLoc);
		
		} else{
			addWeather(weather);
		}
		
		return weather;
	}
	
	
	public boolean hasLocation(Location loc){
		return weatherMap.containsKey(loc);
	}
	
	public void removeRecord(int index){
		weatherMap.remove(index);
	}
	
	public int size(){
		return weatherMap.size();
	}
	
}
