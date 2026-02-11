package dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

import utils.CheckDuplicateFile;
import utils.File;
import utils.FileOperations;

import org.json.JSONObject;

import constants.Queries;
import databasemanager.QueryHandler;
import utils.Resource;
import utils.SnowflakeIdGenerator;

public class ResourceManager {
	public static boolean deleteResource(long resourceId) {

		int rowsAffected = QueryHandler.executeUpdate(Queries.DELETE_RESOURCE, new Object[] { resourceId });
		return rowsAffected > 0;
	}

	public static JSONObject addResource(String resourceName, Long parentId, long userId) throws SQLException, IOException {

		long id = SnowflakeIdGenerator.nextId();
		long currentTime=System.currentTimeMillis();

		if(parentId!=null) {
		    QueryHandler.executeUpdate(Queries.ADD_RESOURCE, new Object[] { id, resourceName, parentId, userId, currentTime, currentTime });
		}else {
			QueryHandler.executeUpdate(Queries.ADD_RESOURCE_ROOT, new Object[] { id, resourceName, userId, currentTime, currentTime });
		}

		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_RESOURCE, new Object[] { id });
		if (rs.next()) {
			ResultSet tempRs=QueryHandler.executeQuerry(Queries.GET_ALL_CONTAINS, new Object[] {rs.getLong("ResourceId"),rs.getLong("ResourceId")});
			
			int totalFiles = 0;
		    int totalFolders = 0;
		    
		    if (tempRs != null && tempRs.next()) {
		        totalFiles = tempRs.getInt("totalFiles");
		        totalFolders = tempRs.getInt("totalFolders");
		    }
			String size = FileOperations.getFolderSize(rs.getLong("ResourceId"));

			Resource resource = new Resource(rs.getLong("ResourceId"), rs.getString("ResourceName"),
					rs.getLong("CreatedTime"),
					rs.getLong("LastModifiedTime"), rs.getLong("parentId"), rs.getString("TimeZone"), totalFiles, totalFolders, size);
			return resource.toJson();
		}
		return null;
	}

	public static boolean updateResource(String newName, long resourceId) {
		long currentTime = System.currentTimeMillis();
		
		int rowsAffected = QueryHandler.executeUpdate(Queries.UPDATE_RESOURCE, new Object[] { newName, currentTime, resourceId});
		return rowsAffected > 0;
	};

	public static ArrayList<JSONObject> getResource(long parentId, long userId, long cursor, int limit) throws SQLException, IOException {
		ArrayList<JSONObject> resources = new ArrayList<>();

		ResultSet rs;
			rs = QueryHandler.executeQuerry(Queries.GET_RESOURCES, new Object[] { userId, parentId, cursor, limit });

		while (rs.next()) {
			ResultSet tempRs = QueryHandler.executeQuerry(Queries.GET_ALL_CONTAINS,
					new Object[] { rs.getLong("ResourceId"), rs.getLong("ResourceId") });

			int totalFiles = 0;
			int totalFolders = 0;

			if (tempRs != null && tempRs.next()) {
				totalFiles = tempRs.getInt("totalFiles");
				totalFolders = tempRs.getInt("totalFolders");
			}
			String size = FileOperations.getFolderSize(rs.getLong("ResourceId"));

			Resource resource = new Resource(rs.getLong("ResourceId"), rs.getString("ResourceName"),
					rs.getLong("CreatedTime"), rs.getLong("LastModifiedTime"), rs.getLong("parentId"),
					rs.getString("TimeZone"), totalFiles, totalFolders, size);
			resources.add(resource.toJson());
		}

		return resources;
	}

	public static boolean existResourceName(long userId, long parentId, String resourceName) throws SQLException {
		ResultSet rs= QueryHandler.executeQuerry(Queries.EXIST_NAME, new Object[] { parentId, resourceName, userId });
		return rs.next();
	}

	public static boolean moveResource(long parentId, long resourceId, String finalName) {
		int rowsAffected = QueryHandler.executeUpdate(Queries.UPDATE_PARENT,
				new Object[] { parentId, finalName, resourceId });

		return rowsAffected > 0;
	}

	public static boolean AddFile(long folderId, String fileName , String size , String CheckSum) {// =====> my updates
		
		long id = SnowflakeIdGenerator.nextId();
		long currentTimeMills = System.currentTimeMillis();

		int i = QueryHandler.executeUpdate(Queries.ADD_NEW_FILE, new Object[] { id , folderId, fileName,currentTimeMills,currentTimeMills, size , CheckSum , "#1"});

		return i > 0;

	}

	public static boolean deleteFile(long folderId, String filename) {// =====> my updates

		int i = QueryHandler.executeUpdate(Queries.DELETE_FILE, new Object[] { filename, folderId });
		return i > 0;

	}

	public static boolean duplicateFile(long folderId, String filename) throws SQLException {// =====> my updates

		ResultSet res = QueryHandler.executeQuerry(Queries.DUPLICATE_FILE_CHECK, new Object[] { filename, folderId });

		return res.next();
	}

	public static ArrayList<JSONObject> getAllFiles(long folderId , long userId, long cursor, int limit ) throws SQLException {
	    ArrayList<JSONObject> files = new ArrayList<JSONObject>();
	    ResultSet result = QueryHandler.executeQuerry(Queries.SHOW_ALL_FILES, new Object[] { folderId, cursor, limit });
	    
	    ResultSet userDetails = QueryHandler.executeQuerry(Queries.GET_TIME_ZONE, new Object[] {userId});
	    
	    String timeZone = "";
	    
	    if(userDetails.next()) {
	    	timeZone = userDetails.getString("TimeZone");
	    }
	    
	    while (result.next()) {
	        files.add(new File(result.getString("filename"), result.getLong("fileCreateTime") , result.getLong("fileEditTime") , result.getString("Size") ,result.getLong("fileId"), timeZone).getFileData());
	    }
	   
	    System.out.println(files);

		return files;
	}

	public static long getMyFolderId(long userId) throws SQLException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_ROOT_ID, new Object[] { userId });
		if(rs.next())
		{
			return rs.getLong("ResourceId");
		}
		return 0;
	}

	public static boolean renameFile(String filename, long fileId) {

		long currentTimeMills = System.currentTimeMillis();
		int res = QueryHandler.executeUpdate(Queries.UPDATE_FILENAME, new Object[] { filename , currentTimeMills , fileId});
		
		return res > 0;
	}

	public static boolean sameFolder(long parentId, long resourceId) throws SQLException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_PARENT_ID, new Object[] { resourceId });
		rs.next();
		return parentId == rs.getLong(1);
	}
	
	public static boolean copyFile(long olderFolderId , long newFolderId) throws SQLException {
		
		ResultSet res = QueryHandler.executeQuerry(Queries.SHOW_ALL_FILES, new Object[] {olderFolderId});
		
		while(res.next()) {
			
			FileOperations.copyFile(String.valueOf(olderFolderId), String.valueOf(newFolderId), res.getString("filename"));
			ResourceManager.AddFile(newFolderId, res.getString("filename") , res.getString("Size") ,res.getString("checksum"));
			
		}
		
		return res!=null ? true : false;
		
	}
	
	public static boolean copyFolder(long parentId, long resourceId, String finalName, long userId) throws SQLException, IOException {
	    JSONObject folder = addResource(finalName, parentId, userId);
	    long tempFolderId = Long.parseLong(folder.getString("resourceId"));

	    copyFile(resourceId, tempFolderId);

	    try {
			copySubfolders(resourceId, tempFolderId, userId);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    return true;
	}

	
	private static void copySubfolders(long currentFolderId, long parentFolderId, long userId) throws SQLException, IOException {

	    ResultSet subfolders = QueryHandler.executeQuerry(Queries.GET_ALL_FOLDER, new Object[] { currentFolderId });

	    while (subfolders.next()) {
	        long subfolderId = subfolders.getLong("resourceId");
	        String subfolderName = subfolders.getString("resourceName");

	        JSONObject subfolder = addResource(subfolderName, parentFolderId, userId);
	        long newSubfolderId = Long.parseLong(subfolder.getString("resourceId"));

	        copyFile(subfolderId, newSubfolderId);
	        copySubfolders(subfolderId, newSubfolderId, userId);
	    }
	}


	public static long findFileId(long folderId , String filename) {
		
		ResultSet result = QueryHandler.executeQuerry(Queries.GET_FILE_ID, new Object[] {folderId , filename});
		
		try {
			if(result.next()) {
				return result.getLong("fileId");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static long findFileIdUsingCheckSum(long folderId , String checkSum) {
		
		ResultSet result = QueryHandler.executeQuerry(Queries.GET_FILE_ID_USING_CHECKSUM, new Object[] {folderId , checkSum});
		
		try {
			if(result.next()) {
				return result.getLong("fileId");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static boolean fileCopy(long olderFolderId , long newFolderId , String filename) {
		
		boolean fileRes , res = false;
		fileRes = FileOperations.copyFile(String.valueOf(olderFolderId), String.valueOf(newFolderId),filename);	
		filename = CheckDuplicateFile.getFileName(newFolderId, filename);
		if(fileRes) {
			try {
				res = AddFile(newFolderId, filename, FileOperations.getFileSize(String.valueOf(newFolderId+"/"+filename)) , "#1");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return res && fileRes;
		
	}
	
	public static boolean fileMove(long fileId , long olderFolderId , long newFolderId , String filename) {
		
		
		try {
			
			boolean fileMoveHdfs = FileOperations.moveFile(String.valueOf(olderFolderId), String.valueOf(newFolderId), filename);
			int fileMoveDb = QueryHandler.executeUpdate(Queries.UPDATE_FILE_PARENT_ID, new Object[] {newFolderId , fileId});
			
			return fileMoveHdfs && fileMoveDb > 0;
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		return false;
		
	}
	
	public static String checkExsistingFile(String checkSum , long folderId) {
		ResultSet res = QueryHandler.executeQuerry(Queries.GET_FILE_CHECKSUM, new Object[] {checkSum , folderId});
		try {
			if(res.next()) {
				return res.getString("checksum");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static boolean getExsistingFileVersion(long fileId , String version) {
		ResultSet res = QueryHandler.executeQuerry(Queries.GET_FILE_VERSION, new Object[] {fileId});
		try {
			if(res.next()) {
				if(res.getString("version").equals(version))
					return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateFileVersion( long fileId, String version) {
		int updateFileVersionResult = QueryHandler.executeUpdate(Queries.UPDATE_FILE_VERSION, new Object[] {version , fileId});
		
		return updateFileVersionResult > 0 ? true : false;
	}
	
	public static boolean checkExsistingFileName(long folderId , String fileName) {
		
		ResultSet res = QueryHandler.executeQuerry(Queries.GET_EXIST_FILE, new Object[] {folderId , fileName});
		
		try {
			if(res.next()) {
				return true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	public static long findOrCreateFolder(String parentId, String folderName, long userId) throws SQLException, NumberFormatException, IOException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.CHECK_FOLDER_EXISTS,
				new Object[] { Long.parseLong(parentId), folderName });
		if (rs.next()) {
			return rs.getLong("ResourceId");
		} else {
			JSONObject resource = ResourceManager.addResource(folderName, Long.parseLong(parentId), userId);
            return resource.getLong("resourceId");
		}
	}
	

	public static boolean fileMove(long fileId, long olderFolderId, long newFolderId, String filename) {

		try {

			boolean fileMoveHdfs = FileOperations.moveFile(String.valueOf(olderFolderId), String.valueOf(newFolderId),
					filename);
			int fileMoveDb = QueryHandler.executeUpdate(Queries.UPDATE_FILE_PARENT_ID,
					new Object[] { newFolderId, fileId });

			return fileMoveHdfs && fileMoveDb > 0;

		} catch (IOException e) {

			e.printStackTrace();

		}

		return false;

	}
}
