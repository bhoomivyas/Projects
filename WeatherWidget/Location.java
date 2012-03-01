
public class Location  {
	
	private String city;
	private String state;
	

	   public Location(String newCity, String newState) {
	       city = newCity;
	       state = newState;
	   }
	   
	   public String getCity(){
		   return city; 
	   }
	   
	   public String getState(){
		   return state; 
	   }
	   
	   public void setCity(String newCity){
			city = newCity;
	   }
		
		public void setState(String newState){
			state = newState;
		}
	   
	   
	   public boolean equals(Object loc){
			Location newLocation = (Location)loc;
	    	if(city.equalsIgnoreCase(newLocation.getCity()) && state.equalsIgnoreCase(newLocation.getState())){
	    			return true;
	    	}
	    	return false;
	   }
	   
	   
	   public int hashCode(){
	    	return city.toLowerCase().hashCode()+state.toLowerCase().hashCode();
	   }
	   
}

