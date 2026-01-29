package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class File {
	
	private String filename;
	private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    
    public File(String filename , LocalDateTime createdTime , LocalDateTime modifiedTime) {
    	
    	this.filename = filename;
    	this.createdTime = createdTime;
    	this.modifiedTime = modifiedTime;
    	
    }
    
    public JSONObject getFileData() {
    	
    	DateTimeFormatter formatter =
    	        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	
    	JSONObject object = new JSONObject(); 	
    	
    	object.put("fileneme", filename);
    	object.put("createTime", createdTime.format(formatter));
    	object.put("modifiedTime", modifiedTime.format(formatter));
    	
    	return object;
    	
    }
    
	
}
