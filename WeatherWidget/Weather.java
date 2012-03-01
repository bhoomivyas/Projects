public class Weather {
	private Location location;
	private int temperature;
	private int maxTemp;
	private int minTemp;
	private boolean warning = false;
	
	public Weather(String cityName, String stateName, int temp, boolean warning){
		location = new Location(cityName, stateName);
		temperature = temp;
		maxTemp = temp;
		minTemp = temp;
		this.warning = warning;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public void setLocation(Location l){
		location = l;
	}
	
	public int getTemp(){
		return temperature;
	}
	
	public void setTemp(int temp){
		temperature = temp;
	}
	
	public int getMaxTemp(){
		return maxTemp;
	}
	
	public void setMaxTemp(int temp){
		maxTemp = temp;
	}
	
	public int getMinTemp(){
		return minTemp;
	}
	
	public void setMinTemp(int temp){
		minTemp = temp;
	}
	
	public boolean getWeatherWarning(){
		return warning;
	}
	
	public void setWeatherWarning(boolean warning){
		this.warning = warning;
	}
	
	 public boolean equals(Object weatherObj){
	    	
			Weather newWeather = (Weather)weatherObj;
		    	
			if((location.getCity()).equalsIgnoreCase(newWeather.getLocation().getCity())){
	
				if((location.getState()).equalsIgnoreCase(newWeather.getLocation().getState())){			
					return true;
				}
			}
			return false;
	}
	 
}
