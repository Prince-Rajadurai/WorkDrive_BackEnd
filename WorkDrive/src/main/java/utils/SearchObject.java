package utils;

import java.sql.ResultSet;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import constants.ColumnNames;
import databasemanager.QueryHandler;

public class SearchObject {

    private long resourceId;
    private String resourceName;
    private String type;
    private long createdTime;
    private long modifiedTime;
    private String timezone;
    private long parentId;

    public SearchObject(long resourceId, String resourceName, String type, long createdTime, long modifiedTime, String timezone, long parentId) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.type = type;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.timezone = timezone;
        this.parentId = parentId;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("resourceId", String.valueOf(resourceId));
        obj.put("resourceName", resourceName);
        obj.put("parentFolderId", String.valueOf(parentId));
        obj.put("type", type);

        obj.put("parentFolderName", getParentFolderName(parentId));
        if ("FOLDER".equals(type)) {
            obj.put("parentFolderPath", getFullFolderPath(resourceId)); 
        } else {
            obj.put("parentFolderPath", getFullFolderPath(parentId));
        }
        if (createdTime == modifiedTime) {
            obj.put("createdTime", TimeConversion.convertMillisToFormattedDate(createdTime, timezone));
        } else {
            obj.put("modifiedTime", TimeConversion.convertMillisToFormattedDate(modifiedTime, timezone));
        }

        return obj;
    }

    private String getParentFolderName(long parentId) {
        try {
            ResultSet rs = QueryHandler.executeQuerry(
                "SELECT ResourceName FROM " + ColumnNames.RESOURCE_TABLE + " WHERE ResourceId = ?",
                new Object[]{parentId}
            );
            if (rs.next()) return rs.getString("ResourceName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Folder";
    }


    private JSONArray getFullFolderPath(long folderId) {
        JSONArray path = new JSONArray();
        try {
            Stack<JSONObject> stack = new Stack<>();
            long currentId = folderId;
            while (currentId != 0) { 
                ResultSet rs = QueryHandler.executeQuerry(
                    "SELECT ResourceId, ResourceName, ParentId FROM " + ColumnNames.RESOURCE_TABLE + " WHERE ResourceId = ?",
                    new Object[]{currentId}
                );

                if (!rs.next()) break;

                JSONObject folderObj = new JSONObject();
                folderObj.put("id", String.valueOf(rs.getLong("ResourceId")));
                folderObj.put("name", rs.getString("ResourceName"));
                stack.push(folderObj);

                currentId = rs.getLong("ParentId");
            }
            while (!stack.isEmpty()) {
                path.put(stack.pop());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}