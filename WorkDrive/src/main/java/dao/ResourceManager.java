package dao;

import java.io.IOException;
import java.lang.module.ModuleDescriptor.Version;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

import utils.CheckDuplicateFile;
import utils.File;
import utils.FileOperations;
import utils.GetUserId;

import org.json.JSONException;
import org.json.JSONObject;

import constants.ColumnNames;
import constants.Queries;
import databasemanager.QueryHandler;
import utils.Resource;
import utils.Resources;
import utils.SearchObject;
import utils.SnowflakeIdGenerator;
import utils.Versions;

public class ResourceManager {
	public static boolean deleteResource(long resourceId) {

		int rowsAffected = QueryHandler.executeUpdate(Queries.DELETE_RESOURCE, new Object[] { resourceId });
		return rowsAffected > 0;
	}

	public static JSONObject addResource(String resourceName, Long parentId, long userId)
			throws SQLException, IOException {

		long id = SnowflakeIdGenerator.nextId();
		long currentTime = System.currentTimeMillis();

		if (parentId != null) {
			QueryHandler.executeUpdate(Queries.ADD_RESOURCE,
					new Object[] { id, resourceName, parentId, userId, currentTime, currentTime, "FOLDER", 0 });
		} else {
			QueryHandler.executeUpdate(Queries.ADD_RESOURCE_ROOT,
					new Object[] { id, resourceName, userId, currentTime, currentTime, "FOLDER", 0 });
		}

		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_RESOURCE, new Object[] { id });
		if (rs.next()) {
			ResultSet tempRs = QueryHandler.executeQuerry(Queries.GET_ALL_CONTAINS,
					new Object[] { rs.getLong("ResourceId"), "FILE", rs.getLong("ResourceId"), "FOLDER" });

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
			return resource.toJson();
		}
		return null;
	}

	public static boolean updateResource(String newName, long resourceId) {
		long currentTime = System.currentTimeMillis();

		int rowsAffected = QueryHandler.executeUpdate(Queries.UPDATE_RESOURCE,
				new Object[] { newName, currentTime, resourceId });
		return rowsAffected > 0;
	};
	
	public static ArrayList<JSONObject> getResources(String type, long parentId, long userId, long cursor, int limit, String sortBy, String sortOrder)
			throws SQLException {
		String sortColumn = ColumnNames.RESOURCE_NAME;
		String sortorder = "ASC";
		if (sortBy.equalsIgnoreCase("name")) {
			sortColumn = ColumnNames.RESOURCE_NAME;
		} else if (sortBy.equalsIgnoreCase("createdTime")) {
			sortColumn = ColumnNames.CREATED_TIME;
		} else if (sortBy.equalsIgnoreCase("lastModifiedTime")) {
			sortColumn = ColumnNames.MODIFIED_TIME;
		}
		if (sortOrder.equalsIgnoreCase("desc")) {
			sortorder = "DESC";
		}
		ArrayList<JSONObject> resources = new ArrayList<>();
		ResultSet userDetails = QueryHandler.executeQuerry(Queries.GET_TIME_ZONE, new Object[] { userId });
		String timeZone = "";
		if (userDetails.next()) {
			timeZone = userDetails.getString("TimeZone");
		}
		if (type.equalsIgnoreCase("Folder")) {
			String cursorCondition = "";
			Object[] params;

			if (cursor > 0) {
			    cursorCondition = " AND ResourceId < ? ";
			    params = new Object[]{ parentId, "FOLDER", "active", cursor, limit };
			} else {
			    params = new Object[]{ parentId, "FOLDER", "active", limit };
			}

			String query = String.format(
			    Queries.GET_RESOURCES_SORTED,
			    cursorCondition,
			    sortColumn,
			    sortorder
			);

			ResultSet folderResultSet = QueryHandler.executeQuerry(query, params);
			while (folderResultSet.next()) {
				ResultSet tempRs = QueryHandler.executeQuerry(Queries.GET_ALL_CONTAINS,
						new Object[] { folderResultSet.getLong("ResourceId"), "FILE",
								folderResultSet.getLong("ResourceId"), "FOLDER" });
				int totalFiles = 0;
				int totalFolders = 0;
				if (tempRs != null && tempRs.next()) {
					totalFiles = tempRs.getInt("totalFiles");
					totalFolders = tempRs.getInt("totalFolders");
				}
				String size = FileOperations.getFolderSize(folderResultSet.getLong("ResourceId"));
				Resource resource = new Resource(folderResultSet.getLong("ResourceId"),
						folderResultSet.getString("ResourceName"), folderResultSet.getLong("CreatedTime"),
						folderResultSet.getLong("LastModifiedTime"), folderResultSet.getLong("parentId"), timeZone,
						totalFiles, totalFolders, size);
				resources.add(resource.toJson());
			}
		} else if (type.equalsIgnoreCase("File")) {
			String cursorCondition = "";
			Object[] params;

			if (cursor > 0) {
			    cursorCondition = " AND ResourceId < ? ";
			    params = new Object[]{ parentId, "FILE", "active", cursor, limit };
			} else {
			    params = new Object[]{ parentId, "FILE", "active", limit };
			}

			String query = String.format(
			    Queries.GET_RESOURCES_SORTED,
			    cursorCondition,
			    sortColumn,
			    sortorder
			);

			ResultSet fileResultSet = QueryHandler.executeQuerry(query, params);
			while (fileResultSet.next()) {
				File file = new File(fileResultSet.getString("ResourceName"), fileResultSet.getLong("CreatedTime"),
						fileResultSet.getLong("LastModifiedTime"), fileResultSet.getLong("ResourceId"),fileResultSet.getLong("originalSize"), timeZone);
				resources.add(file.getFileData());
			}
		}
		return resources;
	}
	
	public static JSONObject getResourceBasicInfo(long resourceId) throws JSONException, SQLException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_RESOURCE_BASIC, new Object[] { resourceId });
		if (rs.next()) {
			JSONObject obj = new JSONObject();
			obj.put("id", rs.getLong("ResourceId"));
			obj.put("name", rs.getString("ResourceName"));
			Long parentId = (Long) rs.getObject("ParentId");
			if (parentId == null) {
				obj.put("parentId", JSONObject.NULL);
			} else {
				obj.put("parentId", parentId);
			}
			return obj;
		}
		return null;
	}

	public static boolean existResourceName(long userId, long parentId, String resourceName) throws SQLException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.EXIST_NAME, new Object[] { parentId, resourceName, userId });
		return rs.next();
	}

	public static boolean moveResource(long parentId, long resourceId, String finalName) {

		int rowsAffected = QueryHandler.executeUpdate(Queries.UPDATE_PARENT,
				new Object[] { parentId, finalName, resourceId });

		return rowsAffected > 0;
	}

	public static long AddFile(long folderId, String fileName, long userId, long original_size) {// =====> my updates

		long id = SnowflakeIdGenerator.nextId();
		long currentTimeMills = System.currentTimeMillis();

		int i = QueryHandler.executeUpdate(Queries.ADD_RESOURCE, new Object[] { id, fileName, folderId, userId,
				currentTimeMills, currentTimeMills, "FILE", original_size });

		return id;

	}

	public static long addDFSFiles(String dfspath, String checksum, long fileid, long folderId, long size) {

		long id = SnowflakeIdGenerator.nextId();

		int i = QueryHandler.executeUpdate(Queries.ADD_DFS_FILES,
				new Object[] { id, dfspath, checksum, fileid, folderId, size });

		return id;

	}

	public static boolean addFileVersion(long dfsId, long size) {

		long id = SnowflakeIdGenerator.nextId();
		long time = System.currentTimeMillis();

		int i = QueryHandler.executeUpdate(Queries.ADD_VERSION, new Object[] { id, 1, dfsId, time, size });

		return i > 0;
	}

	public static String getFilePath(long fileid) {
		
		ResultSet result = QueryHandler.executeQuerry(Queries.GET_FILE_PATH, new Object[] { fileid });

		try {
			if (result.next()) {
				return result.getString(ColumnNames.DFS_PATH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}

	public static boolean deleteFile(long fileId) {// =====> my updates

		int i = QueryHandler.executeUpdate(Queries.DELETE_RESOURCE, new Object[] { fileId });
		return i > 0;

	}

	public static boolean duplicateFile(long folderId, String filename) throws SQLException {// =====> my updates

		ResultSet res = QueryHandler.executeQuerry(Queries.GET_EXISTING_FILE, new Object[] { filename, folderId });

		return res.next();
	}

	public static long getMyFolderId(long userId) throws SQLException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_ROOT_ID, new Object[] { userId });
		if (rs.next()) {
			return rs.getLong("ResourceId");
		}
		return 0;
	}

	public static boolean renameFile(String filename, long fileId, long folderId) {

		long currentTimeMills = System.currentTimeMillis();

		int updateFileNameResult = QueryHandler.executeUpdate(Queries.UPDATE_FILE_NAME,
				new Object[] { filename, currentTimeMills, fileId });

		return updateFileNameResult > 0;
	}

	public static boolean sameFolder(long parentId, long resourceId) throws SQLException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_PARENT_ID, new Object[] { resourceId });
		rs.next();
		return parentId == rs.getLong(1);
	}

	public static boolean copyFile(long olderFolderId, long newFolderId, long userId) throws SQLException {

		ResultSet res = QueryHandler.executeQuerry(Queries.GET_RESOURCES, new Object[] { olderFolderId, "FILE", "active", olderFolderId,30 });
		String filename;
		boolean result = false;

		while (res.next()) {

			filename = res.getString(ColumnNames.RESOURCE_NAME);

			long newFileId, dfsId;

			String filePath = getFilePath(res.getLong(ColumnNames.RESOURCE_ID));
			String fileChecksum = getFileChecksum(res.getLong(ColumnNames.RESOURCE_ID));

			filename = CheckDuplicateFile.getFileName(newFolderId, filename);
			newFileId = AddFile(newFolderId, filename, userId, res.getLong(ColumnNames.RESOURCE_ORIGINAL_SIZE));
			dfsId = addDFSFiles(filePath, fileChecksum, newFileId, newFolderId, FileOperations.getSize(filePath));
			result = addFileVersion(dfsId, FileOperations.getSize(filePath));

		}

		return result;

	}

	public static boolean copyFolder(long parentId, long resourceId, String finalName, long userId)
			throws SQLException, IOException {
		JSONObject folder = addResource(finalName, parentId, userId);
		long tempFolderId = Long.parseLong(folder.getString("resourceId"));

		copyFile(resourceId, tempFolderId, userId);

		try {
			copySubfolders(resourceId, tempFolderId, userId);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	private static void copySubfolders(long currentFolderId, long parentFolderId, long userId)
			throws SQLException, IOException {

		ResultSet subfolders = QueryHandler.executeQuerry(Queries.GET_ALL_FOLDER, new Object[] { currentFolderId, "FOLDER" });

		while (subfolders.next()) {
			long subfolderId = subfolders.getLong("ResourceId");
			String subfolderName = subfolders.getString("ResourceName");

			JSONObject subfolder = addResource(subfolderName, parentFolderId, userId);
			long newSubfolderId = Long.parseLong(subfolder.getString("resourceId"));

			copyFile(subfolderId, newSubfolderId, userId);
			copySubfolders(subfolderId, newSubfolderId, userId);
		}
	}

	public static long findFileId(long folderId, String filename) {

		ResultSet result = QueryHandler.executeQuerry(Queries.GET_FILE_ID, new Object[] { filename, folderId });

		try {
			if (result.next()) {
				return result.getLong(ColumnNames.RESOURCE_ID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long findFileIdUsingFilename(long folderId, String filename) {

		ResultSet result = QueryHandler.executeQuerry(Queries.GET_FILE_ID, new Object[] { filename, folderId });

		try {
			if (result.next()) {
				return result.getLong(ColumnNames.RESOURCE_ID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean fileCopy(long olderFolderId, long newFolderId, String filename, long userId, long fileid) {

		long newFileId, dfsId;
		boolean res;

		String filePath = getFilePath(fileid);
		String fileChecksum = getFileChecksum(fileid);
		long size = ResourceManager.getFileOriginalSize(fileid);

		filename = CheckDuplicateFile.getFileName(newFolderId, filename);
		newFileId = AddFile(newFolderId, filename, userId, size);
		dfsId = addDFSFiles(filePath, fileChecksum, newFileId, newFolderId, FileOperations.getSize(filePath));
		res = addFileVersion(dfsId, FileOperations.getSize(filePath));

		return res;

	}

	public static long getFileSize(long fileId) {

		ResultSet result = QueryHandler.executeQuerry(Queries.FIND_DFS_ID, new Object[] { fileId });

		try {
			if (result.next()) {
				return result.getLong(ColumnNames.DFS_FILE_SIZE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static String getUserTimeZone(long userId) {

		ResultSet res = QueryHandler.executeQuerry(Queries.GET_USER_TIME_ZONE, new Object[] { userId });

		try {
			if (res.next()) {
				return res.getString(ColumnNames.USER_TIMEZONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}

	public static boolean updateFileSize(String checkSum, long size) {

		int res = QueryHandler.executeUpdate(Queries.UPDATE_FILE_SIZE, new Object[] { size, checkSum });

		return res > 0;

	}

	public static String getFileChecksum(long fileid) {

		ResultSet res = QueryHandler.executeQuerry(Queries.FIND_DFS_ID, new Object[] { fileid });

		try {
			if (res.next()) {
				return res.getString(ColumnNames.FILE_CHECKSUM);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static boolean fileMove(long fileId, long olderFolderId, long newFolderId, String filename) {

		try {

			filename = CheckDuplicateFile.getFileName(newFolderId, filename);

			int fileMoveDb = QueryHandler.executeUpdate(Queries.UPDATE_FILE_PARENT_ID,
					new Object[] { newFolderId, filename, fileId });

			return fileMoveDb > 0;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return false;

	}

	public static int checkExistPaths(String filePath) {

		int count = 0;
		ResultSet res = QueryHandler.executeQuerry(Queries.CHECK_EXIST_FILE_PATHS, new Object[] { filePath });

		try {
			while (res.next()) {
				count++;
				if (count == 2) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public static String checkExsistingFile(String checkSum, long folderId) {

		ResultSet res = QueryHandler.executeQuerry(Queries.GET_FILE_CHECKSUM, new Object[] { checkSum, folderId , "active" });
		try {
			if (res.next()) {
				return res.getString(ColumnNames.FILE_CHECKSUM);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getFilePathUsingCheckSum(String checksum) {

		ResultSet res = QueryHandler.executeQuerry(Queries.GET_FILE_PATHS, new Object[] { checksum });
		try {
			if (res.next()) {
				return res.getString(ColumnNames.DFS_PATH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	public static long getFileIdUsingCheckSum(String checkSum, long folderId) {
		ResultSet res = QueryHandler.executeQuerry(Queries.GET_FILE_CHECKSUM, new Object[] { checkSum, folderId , "active" });
		try {
			if (res.next()) {
				return res.getLong(ColumnNames.DFS_FILE_ID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long findDfsId(long fileId) {

		ResultSet res = QueryHandler.executeQuerry(Queries.FIND_DFS_ID, new Object[] { fileId });

		try {
			if (res.next()) {
				return res.getLong(ColumnNames.DFS_ID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	public static int getFileVersion(long dfsId) {

		ResultSet res = QueryHandler.executeQuerry(Queries.GET_FILE_VERSION, new Object[] { dfsId });

		try {
			if (res.next()) {
				return res.getInt("max_version");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static boolean addNewFileVersion(long dfsId, int version, String path) {

		long id = SnowflakeIdGenerator.nextId();
		long time = System.currentTimeMillis();

		int updateFileVersionResult = QueryHandler.executeUpdate(Queries.ADD_VERSION,
				new Object[] { id, version, dfsId, time, FileOperations.getSize(path) });

		return updateFileVersionResult > 0 ? true : false;
	}

	public static boolean checkExsistingFileName(long folderId, String fileName) {

		ResultSet res = QueryHandler.executeQuerry(Queries.GET_EXISTING_FILE, new Object[] { folderId, fileName , "active"});

		try {
			if (res.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	public static ArrayList<JSONObject> getAllVersions(long dfsId, String timeZone) {

		ArrayList<JSONObject> version = new ArrayList<JSONObject>();
		ResultSet res = QueryHandler.executeQuerry(Queries.GET_ALL_FILE_VERSIONS, new Object[] { dfsId });

		try {
			while (res.next()) {
				version.add(new Versions(res.getInt(ColumnNames.VERSION_NUMBER),
						res.getLong(ColumnNames.VERSION_CREATE_TIME), res.getLong(ColumnNames.VERSION_SIZE), timeZone)
						.getVersionData());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return version;
	}

	public static long findOrCreateFolder(String parentId, String folderName, long userId)
			throws SQLException, NumberFormatException, IOException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.CHECK_FOLDER_EXISTS,
				new Object[] { Long.parseLong(parentId), folderName });
		if (rs.next()) {
			return rs.getLong("ResourceId");
		} else {
			JSONObject resource = ResourceManager.addResource(folderName, Long.parseLong(parentId), userId);
			return resource.getLong("resourceId");
		}
	}

	public static long getOriginalSize(long userId) {

		try {
			ResultSet res = QueryHandler.executeQuerry(Queries.GET_ALL_FILES_ORIGINAL_SIZE,
					new Object[] { userId, "FILE"});
			if (res.next()) {
				return res.getLong("total_original_size");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	public static long getCompressedSize(long userId) {

		try {
			ResultSet res = QueryHandler.executeQuerry(Queries.GET_ALL_FILES_COMPRESSED_SIZE, new Object[] { userId});
			if (res.next()) {
				return res.getLong("total_compress_size");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	public static long getDeduplicateFiles(long userId) {

		try {
			ResultSet res = QueryHandler.executeQuerry(Queries.GET_ALL_DEDUPLICATE_FILES, new Object[] { userId});
			if (res.next()) {
				return res.getLong("unique_paths_count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	public static long getTotalFiles(long userId , String status) {

		try {
			ResultSet res = QueryHandler.executeQuerry(Queries.FIND_ALL_FILES, new Object[] { userId, status});
			if (res.next()) {
				return res.getLong("total_files");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	public static long getFileOriginalSize(long fileId) {

		ResultSet res = QueryHandler.executeQuerry(Queries.FILE_ORIGINAL_SIZE, new Object[] { fileId });

		try {
			if (res.next()) {
				return res.getLong(ColumnNames.RESOURCE_ORIGINAL_SIZE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	public static boolean updateDfsPath(String path, String checkSum, long fileId) {
		int i = QueryHandler.executeUpdate(Queries.UPDATE_DFS_PATH,
				new Object[] { path, checkSum, FileOperations.getSize(path), fileId });
		return i > 0;
	}

	public static long getFileIdUsingFileName(long folderId, String filename) {
		ResultSet res = QueryHandler.executeQuerry(Queries.GET_FILE_ID_USING_FILE_NAME,
				new Object[] { folderId, filename });
		try {
			if (res.next()) {
				return res.getLong(ColumnNames.RESOURCE_ID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long getVersionSize(long dfsId) {

		ResultSet res = QueryHandler.executeQuerry(Queries.GET_FILE_VERSIONS_SIZE, new Object[] { dfsId });

		try {
			if (res.next()) {
				return res.getLong("total_size");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	public static long getDuplicateFilesSize(long userId) {
		ResultSet res = QueryHandler.executeQuerry(Queries.GET_DEDUPLICATE_FILES_SIZES, new Object[] { userId});
		long size = 0;
		try {
			while (res.next()) {
				size += res.getLong("size");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return size;
	}

	public static boolean updateFileStatus(long fileid, String status) {

		int result = 0;
		try {
			result = QueryHandler.executeUpdate(Queries.UPDATE_FILE_STATUS, new Object[] { status, fileid, fileid });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result > 0;

	}
	
	public static boolean updateChild(long folderId) {

		int result = 0;
		try {
			result = QueryHandler.executeUpdate(Queries.UPDATE_CHILDS, new Object[] { "active" , folderId , "ParentInactive" });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static ArrayList<JSONObject> showTrashItems(long userId, String timezone) {

		ResultSet res = QueryHandler.executeQuerry(Queries.SELECT_ALL_TRASH_FILES, new Object[] { userId, "inactive" });
		ArrayList<JSONObject> trashResources = new ArrayList<JSONObject>();
		long innerChildSize = 0;

		try {
			while (res.next()) {
				if(res.getString(ColumnNames.RESOURCE_TYPE).equals("FILE")) {
					trashResources.add(new Resources(res.getString(ColumnNames.RESOURCE_NAME), res.getLong(ColumnNames.PARENT_ID),
							res.getLong(ColumnNames.RESOURCE_ORIGINAL_SIZE), res.getLong(ColumnNames.RESOURCE_ID),
							res.getLong(ColumnNames.MODIFIED_TIME),res.getString(ColumnNames.RESOURCE_TYPE), timezone).getJsonData());
				}
				else {
					ResultSet result = QueryHandler.executeQuerry(Queries.SELECT_ALL_INACTIVE_CHILD, new Object[] {userId , res.getLong(ColumnNames.RESOURCE_ID) , "FILE" , "ParentInactive"});
					if(result.next()) {
						innerChildSize = result.getLong("size");
					}
					trashResources.add(new Resources(res.getString(ColumnNames.RESOURCE_NAME), res.getLong(ColumnNames.PARENT_ID),
							innerChildSize, res.getLong(ColumnNames.RESOURCE_ID),
							res.getLong(ColumnNames.MODIFIED_TIME),res.getString(ColumnNames.RESOURCE_TYPE), timezone).getJsonData());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return trashResources;
	}

	public static long getTrashItems(long userId) {
		ResultSet res = QueryHandler.executeQuerry(Queries.TOTAL_TRASH_RESOURCES, new Object[] { userId, "inactive" });
		try {
			if (res.next()) {
				return res.getLong("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long getParticularResource(long userId, String type) {
		ResultSet res = QueryHandler.executeQuerry(Queries.FIND_PARTCULAR_RESOURCE_COUNT,
				new Object[] { userId, "inactive", type });
		try {
			if (res.next()) {
				return res.getLong("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long getTrashSize(long userId) {
		ResultSet res = QueryHandler.executeQuerry(Queries.GET_ALL_FILES_TRASH_SIZE,
				new Object[] { userId, "inactive" });
		try {
			if (res.next()) {
				return res.getLong("size");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean checkResource(long id, String status) {
		ResultSet res = QueryHandler.executeQuerry(Queries.CHECK_RESOURCES, new Object[] { id, status });
		try {
			if (res.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean updateParent(long fileId,long folderId, long userId) {
		int res = QueryHandler.executeUpdate(Queries.UPDATE_PARENT_ID, new Object[] { folderId,fileId, userId });
		return res > 0;
	}

	public static boolean updateFolderStatus(long folderId) {
		int res = QueryHandler.executeUpdate(Queries.UPDATE_FOLDER_STATUS, new Object[] { "inactive", folderId });
		int res1 = QueryHandler.executeUpdate(Queries.UPDATE_FOLDER_FILES_STATUS,new Object[] { "ParentInactive", folderId, "active" });
		return true;
	}
	
	public static JSONObject getSearchResources(String param, long userId) throws SQLException {
		ArrayList<JSONObject> files=new ArrayList<>();
		ArrayList<JSONObject> folders=new ArrayList<>();
		ResultSet rs=QueryHandler.executeQuerry(Queries.SEARCH_RESOURCES, new Object[] {userId,param,param,param,param,param,param});
		
		while(rs.next()) {
			if(rs.getString("type").equals("FILE")) {
				files.add(new SearchObject(rs.getLong("ResourceId"),rs.getString("ResourceName"),rs.getString("type"),rs.getLong("CreatedTime"),rs.getLong("LastModifiedTime"),rs.getString("TimeZone"),rs.getLong("ParentId")).toJson());
			}else {
				folders.add(new SearchObject(rs.getLong("ResourceId"),rs.getString("ResourceName"),rs.getString("type"),rs.getLong("CreatedTime"),rs.getLong("LastModifiedTime"),rs.getString("TimeZone"),rs.getLong("ParentId")).toJson());
			}
		}
		
		JSONObject obj=new JSONObject();
		obj.put("folders", folders);
		obj.put("files", files);
		return obj;
	}
	
	public static boolean updateFolder(long folderId) {
		int res = QueryHandler.executeUpdate(Queries.UPDATE_FOLDER_STATUS, new Object[] { "active", folderId });
		return res>0;
	}
	
	public static long getSize(long fileId) {
		ResultSet res = QueryHandler.executeQuerry(Queries.GET_SIZE, new Object[] {fileId});
		try {
			if(res.next()) {
				return res.getLong(ColumnNames.RESOURCE_ORIGINAL_SIZE);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}