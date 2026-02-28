package utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import dao.ResourceManager;

public class File {
	
	private String filename;
	private long createdTime;
    private long modifiedTime;
    private Long fileId;
    private String timeZone;
    private long size;
    
    public File(String filename , long createdTime , long modifiedTime , long fileId ,long size, String timeZone) {
    	
    	this.filename = filename;
    	this.createdTime = createdTime;
    	this.modifiedTime = modifiedTime;
    	this.fileId = fileId;
    	this.size = size;
    	this.timeZone = timeZone;
    	
    }
    
    public JSONObject getFileData() {
    	
    	DateTimeFormatter formatter =
    	        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	
    	JSONObject resObject = new JSONObject(); 

    	
    	resObject.put("filename", filename);
    	resObject.put("createTime", TimeConversion.convertMillisToFormattedDate(createdTime, timeZone));
    	resObject.put("modifiedTime", TimeConversion.convertMillisToFormattedDate(modifiedTime, timeZone));
    	resObject.put("size", FileOperations.converFileSizeToString(size));
    	resObject.put("id", String.valueOf(fileId));
    	
    	return resObject;
    	
    }
    
    public static JSONObject addingJson(JSONObject file) {
    	JSONObject obj = new JSONObject();
    	obj.put("id", String.valueOf(file.getLong("id")));
        obj.put("name", file.getString("filename"));
        obj.put("type", "FILE");
        obj.put("createdTime", file.getString("createTime"));
        obj.put("modifiedTime", file.getString("modifiedTime"));
        obj.put("size", file.getString("size"));
        return obj;
    }
    
	
}
