package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utils.File;
import org.json.JSONObject;

import constants.ColumnNames;
import constants.Queries;
import databasemanager.QueryHandler;
import utils.Resource;
import utils.SnowflakeIdGenerator;

public class ResourceManager {

	public static boolean deleteResource(long resourceId) {

		int rowsAffected = QueryHandler.executeUpdate(Queries.DELETE_RESOURCE, new Object[] { resourceId, resourceId });
		return rowsAffected > 0;
	}

	public static JSONObject addResource(String resourceName, Long parentId, long userId) throws SQLException {

		long id = SnowflakeIdGenerator.nextId();

		QueryHandler.executeUpdate(Queries.ADD_RESOURCE, new Object[] { id, resourceName, parentId, userId });

		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_RESOURCE, new Object[] { id });
		if (rs.next()) {
			Resource resource = new Resource(rs.getLong("ResourceId"), rs.getString("ResourceName"),
					rs.getTimestamp("CreatedTime").toLocalDateTime(),
					rs.getTimestamp("LastModifiedTime").toLocalDateTime(), rs.getLong("parentId"));
			return resource.toJson();
		}
		return null;
	}

	public static boolean updateResource(String newName, long resourceId) {
		int rowsAffected = QueryHandler.executeUpdate(Queries.UPDATE_RESOURCE, new Object[] { newName, resourceId });
		return rowsAffected > 0;
	};

	public static ArrayList<JSONObject> getResource(Long parentId, long userId) throws SQLException {
		ArrayList<JSONObject> resources = new ArrayList<>();

		ResultSet rs;
			rs = QueryHandler.executeQuerry(Queries.GET_RESOURCES, new Object[] { userId, parentId });

		while (rs.next()) {

			Resource resource = new Resource(rs.getLong("ResourceId"), rs.getString("ResourceName"),
					rs.getTimestamp("CreatedTime").toLocalDateTime(),
					rs.getTimestamp("LastModifiedTime").toLocalDateTime(), rs.getLong("parentId"));
			resources.add(resource.toJson());
		}

		return resources;
	}

	public static boolean existResourceName(long userId, Long parentId, String resourceName) throws SQLException {
		ResultSet rs;
		if (parentId != null) {
			rs = QueryHandler.executeQuerry(Queries.EXIST_NAME, new Object[] { parentId, resourceName, userId });
		} else {
			rs = QueryHandler.executeQuerry(Queries.EXIST_NAME_ROOT, new Object[] { resourceName, userId });
		}
		return rs.next();

	}

	public static boolean moveResource(Long parentId, long resourceId, String finalName) {
		int rowsAffected = QueryHandler.executeUpdate(Queries.UPDATE_PARENT,
				new Object[] { parentId, finalName, resourceId });

		return rowsAffected > 0;
	}

	public static boolean AddFile(long folderId, String filePath, String fileName) {// =====> my updates

		int i = QueryHandler.executeUpdate(Queries.ADD_NEW_FILE, new Object[] { folderId, fileName, filePath });

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

	public static ArrayList<JSONObject> getAllFiles(long folderId) throws SQLException {

		ArrayList<JSONObject> files = new ArrayList<JSONObject>();

		ResultSet result = QueryHandler.executeQuerry(Queries.SHOW_ALL_FILES, new Object[] { folderId });

		while (result.next()) {
			files.add(new File(result.getString("filename"), result.getTimestamp("fileCreateTime").toLocalDateTime(),
					result.getTimestamp("fileEditTime").toLocalDateTime()).getFileData());
		}

		return files;

	}

	public static long getMyFolderId(long userId) throws SQLException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_ROOT_ID, new Object[] { userId });
		rs.next();
		return rs.getLong("ResourceId");
	}

	public static boolean renameFile(String filename, long folderId) {

		int res = QueryHandler.executeUpdate(filename, new Object[] { filename, folderId });

		return res > 0;
	}

	public static boolean sameFolder(long parentId, long resourceId) throws SQLException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_PARENT_ID, new Object[] { resourceId });
		rs.next();
		return parentId == rs.getLong(1);
	}

}
