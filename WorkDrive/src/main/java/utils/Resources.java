package utils;

import org.json.JSONObject;

public class Resources {
	
	String name , timezone , type;
	long  folderId , size , resourcesId , time;
	
	public Resources(String name , long folderId , long size , long resourcesId , long time ,String type, String timezone) {
		this.name = name;
		this.folderId = folderId;
		this.size = size;
		this.resourcesId = resourcesId;
		this.time = time;
		this.type = type;
		this.timezone = timezone;
	}
	
	public JSONObject getJsonData() {
		
		JSONObject obj = new JSONObject();

		obj.put("name", name);
		obj.put("ResourceId", String.valueOf(resourcesId));
		obj.put("folderId", String.valueOf(folderId));
		obj.put("size", FileOperations.converFileSizeToString(size));
		obj.put("time", TimeConversion.convertMillisToFormattedDate(time, timezone));
		obj.put("type", type);
		
		return obj;
	}
	
	public static JSONObject addingJson(JSONObject resource) {
		JSONObject obj = new JSONObject();
        obj.put("id", String.valueOf(resource.getLong("resourceId")));
        obj.put("name", resource.getString("resourceName"));
        obj.put("type", "FOLDER");
        obj.put("createdTime", resource.getString("createdTime"));
        obj.put("modifiedTime", resource.getString("modifiedTime"));
        obj.put("size", resource.getString("size"));
        System.out.println("Resource"+resource.getString("size"));
        obj.put("files", resource.getInt("files"));
        obj.put("folders", resource.getInt("folders"));
		return obj;
	}
}
