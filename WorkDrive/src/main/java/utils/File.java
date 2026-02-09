package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class File {
	
	private String filename;
	private long createdTime;
    private long modifiedTime;
    private String size;
    private Long fileId;
    private String timeZone;
    
    public File(String filename , long createdTime , long modifiedTime , String size , long fileId , String timeZone) {
    	
    	this.filename = filename;
    	this.createdTime = createdTime;
    	this.modifiedTime = modifiedTime;
    	this.size = size;
    	this.fileId = fileId;
    	this.timeZone = timeZone;
    	
    }
    
    public JSONObject getFileData() {
    	
    	DateTimeFormatter formatter =
    	        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	
    	JSONObject resObject = new JSONObject(); 	
    	
    	resObject.put("filename", filename);
    	resObject.put("createTime", TimeConversion.convertMillisToFormattedDate(createdTime, timeZone));
    	resObject.put("modifiedTime", TimeConversion.convertMillisToFormattedDate(modifiedTime, timeZone));
    	resObject.put("size", size);
    	resObject.put("id", fileId);
    	
    	return resObject;
    	
    }
    
	
}
