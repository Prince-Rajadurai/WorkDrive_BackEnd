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
    
    public File(String filename , long createdTime , long modifiedTime , long fileId , String timeZone) {
    	
    	this.filename = filename;
    	this.createdTime = createdTime;
    	this.modifiedTime = modifiedTime;
    	this.fileId = fileId;
    	this.timeZone = timeZone;
    	
    }
    
    public JSONObject getFileData() {
    	
    	DateTimeFormatter formatter =
    	        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	
    	JSONObject resObject = new JSONObject(); 
    	
    	String filePath = ResourceManager.getFilePath(fileId);

    	
    	resObject.put("filename", filename);
    	resObject.put("createTime", TimeConversion.convertMillisToFormattedDate(createdTime, timeZone));
    	resObject.put("modifiedTime", TimeConversion.convertMillisToFormattedDate(modifiedTime, timeZone));
    	try {
			resObject.put("size", FileOperations.getFileSize(filePath));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
    	resObject.put("id", String.valueOf(fileId));
    	
    	return resObject;
    	
    }
    
	
}
