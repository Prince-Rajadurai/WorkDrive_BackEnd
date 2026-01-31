package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class File {
	
	private String filename;
	private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private String size;
    
    public File(String filename , LocalDateTime createdTime , LocalDateTime modifiedTime , String size) {
    	
    	this.filename = filename;
    	this.createdTime = createdTime;
    	this.modifiedTime = modifiedTime;
    	this.size = size;
    	
    }
    
    public JSONObject getFileData() {
    	
    	DateTimeFormatter formatter =
    	        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	
    	JSONObject resObject = new JSONObject(); 	
    	
    	resObject.put("filename", filename);
    	resObject.put("createTime", createdTime.format(formatter));
    	resObject.put("modifiedTime", modifiedTime.format(formatter));
    	resObject.put("size", size);
    	
    	return resObject;
    	
    }
    
	
}
