package utils;

import org.json.JSONObject;

public class SearchObject {
	
	private long resourceId;
	private String resourceName;
	private String type;
	
	public SearchObject(long resourceId,String resourceName,String type){
		this.resourceId=resourceId;
		this.resourceName=resourceName;
		this.type=type;
	}
	
	public JSONObject toJson() {
		JSONObject obj=new JSONObject();
		obj.put("resourceId", resourceId);
		obj.put("resourceName", resourceName);
		obj.put("type", type);
		return obj;
	}

}
