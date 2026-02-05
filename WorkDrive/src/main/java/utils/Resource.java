package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class Resource {

    private long resourceId;
    private String resourceName;
    private long createdTime;
    private long modifiedTime;
    private Long parentId;
    private String timeZone;

    public Resource(long resourceId, String resourceName, long createdTime, long modifiedTime,
                    long parentId, String timeZone) {
        this.resourceId = resourceId;
        this.resourceName=resourceName;
        this.createdTime=createdTime;
        this.modifiedTime=modifiedTime;
        this.parentId = parentId;
        this.timeZone=timeZone;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("resourceId", String.valueOf(resourceId));
        obj.put("resourceName", resourceName);
        obj.put("createdTime", TimeConversion.convertMillisToFormattedDate(createdTime,timeZone));
        obj.put("modifiedTime", TimeConversion.convertMillisToFormattedDate(modifiedTime,timeZone));
        obj.put("parentId", String.valueOf(parentId));
        return obj;
    }
}

