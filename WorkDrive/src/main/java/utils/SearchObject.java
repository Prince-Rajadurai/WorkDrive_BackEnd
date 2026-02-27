package utils;

import org.json.JSONObject;

public class SearchObject {

	private long resourceId;
	private String resourceName;
	private String type;
	private long createdTime;
	private long modifiedTime;
	private String timezone;

	public SearchObject(long resourceId, String resourceName, String type, long createdTime, long modifiedTime,String timezone) {
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.type = type;
		this.createdTime = createdTime;
		this.modifiedTime = modifiedTime;
		this.timezone=timezone;
	}

	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		obj.put("resourceId", String.valueOf(resourceId));
		obj.put("resourceName", resourceName);
		obj.put("type", type);
		if (createdTime == modifiedTime) {
			obj.put("createdTime", TimeConversion.convertMillisToFormattedDate(createdTime,timezone));
		} else {
			obj.put("modifiedTime", TimeConversion.convertMillisToFormattedDate(modifiedTime,timezone));
		}
		return obj;
	}

}
