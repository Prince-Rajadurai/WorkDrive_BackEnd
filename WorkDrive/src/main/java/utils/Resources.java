package utils;

import org.json.JSONObject;

public class Resources {
	
	String name , timezone;
	long  folderId , size , resourcesId , time;
	
	public Resources(String name , long folderId , long size , long resourcesId , long time , String timezone) {
		this.name = name;
		this.folderId = folderId;
		this.size = size;
		this.resourcesId = resourcesId;
		this.time = time;
		this.timezone = timezone;
	}
	
	public JSONObject getJsonData() {
		
		JSONObject obj = new JSONObject();

		obj.put("name", name);
		obj.put("ResourceId", String.valueOf(resourcesId));
		obj.put("folderId", String.valueOf(folderId));
		obj.put("size", FileOperations.converFileSizeToString(size));
		obj.put("time", TimeConversion.convertMillisToFormattedDate(time, timezone));
		
		return obj;
	}
}
