package utils;

import org.json.JSONObject;


public class Versions {
	
	int version;
	long time,size;
	String timeZone;
	
	public Versions(int version , long time ,long size, String timeZone) {
		this.version = version;
		this.time = time;
		this.timeZone = timeZone;
		this.size = size;
	}
	
	public JSONObject getVersionData() {
    	
    	JSONObject resObject = new JSONObject(); 
    	
    	resObject.put("version", version);
    	resObject.put("time", TimeConversion.convertMillisToFormattedDate(time, timeZone));
    	resObject.put("size", FileOperations.converFileSizeToString(size));
    	return resObject;
		
	}

}
