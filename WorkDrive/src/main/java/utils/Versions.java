package utils;

import org.json.JSONObject;


public class Versions {
	
	int version;
	long time;
	String timeZone;
	
	public Versions(int version , long time , String timeZone) {
		this.version = version;
		this.time = time;
		this.timeZone = timeZone;
	}
	
	public JSONObject getVersionData() {
    	
    	JSONObject resObject = new JSONObject(); 
    	
    	resObject.put("version", version);
    	resObject.put("time", TimeConversion.convertMillisToFormattedDate(time, timeZone));
    	
    	return resObject;
		
	}

}
