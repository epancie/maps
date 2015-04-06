package ar.net.epancie.fibertelzonemaps;

public class AccessPointData {

	private double lat, lng;
	private String locationType;
	
	public AccessPointData(double lat, double lng, String locationType){
		this.lat = lat;
		this.lng = lng;
		this.locationType = locationType;
	}
	
	public double getLat(){
		return lat;
	}
	
	public double getLng(){
		return lng;
	}
	
	public String getLocationType(){
		return locationType;
	}
	
	
	public void setLat(double lat){
		this.lat = lat;
	}
	
	public void setLng(double lng){
		this.lng = lng;
	}
	
	public void setLocationType(String locationType){
		this.locationType = locationType;
	}
}
