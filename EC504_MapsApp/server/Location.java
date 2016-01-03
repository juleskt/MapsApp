
public class Location implements IPoint {
	
	double lat, lng;
	String state="", county="";
	
	// Constructor for queries
	public Location(double lat, double lng) {
		this.lat=lat;
		this.lng=lng;
		
	}
	
	// Constructor to store more info
	public Location(String state, String county, double lat, double lng) {
		this.lat=lat;
		this.lng=lng;
		this.state=state;
		this.county=county;
		
	}
	
	// Getters
	public double getX() {
		return lat;
	}
	
	public double getY() {
		return lng;
	}
	
	public String getState() {
		return state;
	}

	public String getCounty() {
		return county;
	}
	
	// Printable format
	public String toString() {
		if (state.equals("") || county.equals("")) {
			return lat+" "+lng;
		}
		return state+" "+county+" "+lat+" "+lng;
	}


}
