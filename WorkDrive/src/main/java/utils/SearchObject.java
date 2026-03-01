package utils;

import org.json.JSONObject;

public class SearchObject {

	private long resourceId;
	private String resourceName;
	private String type;
	private long createdTime;
	private long modifiedTime;
	private String timezone;
	private long parentId;

	public SearchObject(long resourceId, String resourceName, String type, long createdTime, long modifiedTime,String timezone,long parentId) {
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.type = type;
		this.createdTime = createdTime;
		this.modifiedTime = modifiedTime;
		this.timezone=timezone;
		this.parentId=parentId;
	}

	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		obj.put("resourceId", String.valueOf(resourceId));
		obj.put("resourceName", resourceName);
		obj.put("parentId", String.valueOf(parentId));
		obj.put("type", type);
		if (createdTime == modifiedTime) {
			obj.put("createdTime", TimeConversion.convertMillisToFormattedDate(createdTime,timezone));
		} else {
			obj.put("modifiedTime", TimeConversion.convertMillisToFormattedDate(modifiedTime,timezone));
		}
		return obj;
	}

}
