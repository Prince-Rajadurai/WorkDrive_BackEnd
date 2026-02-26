package constants;

import static constants.ColumnNames.*;

public class Queries {


	public static final String GET_USERID="SELECT "+USER_ID+" FROM "+USERS_TABLE+" WHERE "+USER_EMAIL+" = ?";
	
	public static final String CHECK_DUPLICATE_USER = "SELECT "+USER_EMAIL+" FROM "+USERS_TABLE+" WHERE "+USER_EMAIL+"=?";
	
	public static final String GET_USER_TIME_ZONE = "SELECT * FROM "+USERS_TABLE+" WHERE "+USER_ID+" = ?";
	
	public static final String ADD_USER = "INSERT INTO "+USERS_TABLE+" ("+USER_ID+", "+USER_EMAIL+", FullName, "+USER_PASSWORD+", "+USER_TIMEZONE+") VALUES (?, ?, ?, ?, ?)";
	
	public static final String VALIDATE_USER = "SELECT COUNT(*) FROM "+USERS_TABLE+" WHERE "+USER_EMAIL+" = ? AND "+USER_PASSWORD+" = ?";

	public static final String GET_RESOURCES="SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ? AND "+RESOURCE_TYPE+" = ? AND "+RESOURCE_STATUS+" = ? AND "+RESOURCE_ID+" > ? ORDER BY "+RESOURCE_ID+" ASC LIMIT ?";
	
	public static final String GET_RESOURCES_SORTED="SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ? AND "+RESOURCE_TYPE+" = ? AND "+RESOURCE_STATUS+" = ? AND "+RESOURCE_ID+" > ? ORDER BY %s %s LIMIT ?";
	
	public static final String GET_ROOT_ID= "SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" IS NULL AND "+USER_ID+" = ?";
	
	public static final String GET_PARENT_ID = "SELECT "+PARENT_ID+" FROM "+RESOURCE_TABLE+" WHERE "+RESOURCE_ID+" = ? ;";
	
	public static final String DELETE_RESOURCE = "DELETE FROM "+RESOURCE_TABLE+" WHERE "+RESOURCE_ID+" = ?;";
		
	public static final String ADD_RESOURCE = "INSERT INTO "+RESOURCE_TABLE+" ("+RESOURCE_ID+", "+RESOURCE_NAME+", "+PARENT_ID+", "+USER_ID+", "+CREATED_TIME+", "+MODIFIED_TIME+" , "+RESOURCE_TYPE+" , "+RESOURCE_ORIGINAL_SIZE+" ) VALUES (?,?,?,?,?,?,?,?)";
		
	public static final String ADD_RESOURCE_ROOT = "INSERT INTO "+RESOURCE_TABLE+" ("+RESOURCE_ID+", "+RESOURCE_NAME+", "+USER_ID+", "+CREATED_TIME+", "+MODIFIED_TIME+", "+RESOURCE_TYPE+", "+RESOURCE_ORIGINAL_SIZE+") VALUES (?,?,?,?,?,?,?)";
	
	public static final String UPDATE_RESOURCE="UPDATE "+RESOURCE_TABLE+" SET "+RESOURCE_NAME+" = ?, "+MODIFIED_TIME+" = ? WHERE "+RESOURCE_ID+" = ?";
	
	public static final String GET_RESOURCE="SELECT r.*, u."+USER_TIMEZONE+" FROM "+RESOURCE_TABLE+" r JOIN Users u ON r.UserId = u.UserId WHERE r."+RESOURCE_ID+" = ?";
	
	public static final String GET_ALL_FOLDER="SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ? AND "+RESOURCE_TYPE+" = ?";
		
	public static final String UPDATE_PARENT = "UPDATE "+RESOURCE_TABLE+" SET "+PARENT_ID+" = ?,"+RESOURCE_NAME+" = ? WHERE "+RESOURCE_ID+" = ?";
	
	public static final String EXIST_NAME = "SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ? AND "+RESOURCE_NAME+" = ? AND "+USER_ID+"= ? ;";
	
	public static final String GET_TIME_ZONE = "SELECT * FROM "+USERS_TABLE+" WHERE "+USER_ID+" = ?";
	
	public static final String GET_ALL_CONTAINS = "SELECT (SELECT COUNT(*) FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ? AND "+RESOURCE_TYPE+" = ? ) AS totalFiles, (SELECT COUNT(*) FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ? AND "+RESOURCE_TYPE+" = ? ) AS totalFolders "; 
	
	public static final String ADD_DFS_FILES = "INSERT INTO "+DFS_TABLE_NAME+" VALUES ( ? , ? , ? , ? ,? , ?)";
	
	public static final String ADD_VERSION = "INSERT INTO "+VERSION_TABLE_NAME+" VALUES ( ? , ? , ? , ? , ?)";
	
	public static final String CHECK_FOLDER_EXISTS="SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ? AND "+RESOURCE_NAME+" = ? ";
	
	public static final String GET_FILE_ID = "SELECT * FROM "+RESOURCE_TABLE+" WHERE "+RESOURCE_NAME+" = ? AND "+PARENT_ID+" = ?";
	
	public static final String UPDATE_FILE_PATH = "UPDATE "+DFS_TABLE_NAME+" SET "+DFS_PATH+" = ? WHERE "+DFS_FILE_ID+" = ?";
	
	public static final String UPDATE_FILE_NAME = "UPDATE "+RESOURCE_TABLE+" SET "+RESOURCE_NAME+" = ? , "+MODIFIED_TIME+" = ? WHERE "+RESOURCE_ID+" = ?";
	
	public static final String GET_FILE_PATH = "SELECT * FROM "+DFS_TABLE_NAME+" WHERE "+DFS_FILE_ID+" = ? ";
	
	public static final String GET_EXISTING_FILE = "SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ? AND "+RESOURCE_NAME+" = ? AND "+RESOURCE_STATUS+" = ?";
	
	public static final String GET_FILE_CHECKSUM = "SELECT * FROM "+DFS_TABLE_NAME+" D JOIN "+RESOURCE_TABLE+" R ON R."+RESOURCE_ID+" = D."+DFS_FILE_ID+" WHERE D."+FILE_CHECKSUM+" = ? AND D."+DFS_FOLDER_ID+" = ? AND R."+RESOURCE_STATUS+" = ?";
	
	public static final String FIND_DFS_ID = "SELECT * FROM "+DFS_TABLE_NAME+" WHERE "+DFS_FILE_ID+" = ? ";
	
	public static final String UPDATE_FILE_VERSION = "UPDATE "+VERSION_TABLE_NAME+" SET "+VERSION_NUMBER+" = ? WHERE "+VERSION_DFS_ID+" = ?";
	
	public static final String GET_FILE_VERSION = "SELECT MAX(" + VERSION_NUMBER + ") AS max_version FROM " + VERSION_TABLE_NAME +" WHERE " + VERSION_DFS_ID + " = ?";
	
	public static final String GET_ALL_FILES = "SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ? AND "+RESOURCE_TYPE+" = ?";
	
	public static final String UPDATE_FILE_PARENT_ID = "UPDATE "+RESOURCE_TABLE+" SET "+PARENT_ID+" = ? , "+RESOURCE_NAME+" = ? WHERE "+RESOURCE_ID+" = ?";
	
	public static final String CHECK_EXIST_FILE_PATHS = "SELECT * FROM "+DFS_TABLE_NAME+" WHERE "+DFS_PATH+" = ? ";
	
	public static final String GET_FILE_PATHS = "SELECT * FROM "+DFS_TABLE_NAME+" WHERE "+FILE_CHECKSUM+" = ? ";
	
	public static final String UPDATE_FILE_SIZE = "UPDATE "+DFS_TABLE_NAME+" SET "+DFS_FILE_SIZE+" = ? WHERE "+FILE_CHECKSUM+" = ?";
	
	public static final String GET_ALL_FILE_VERSIONS = "SELECT * FROM "+VERSION_TABLE_NAME+" WHERE "+VERSION_DFS_ID+" = ? ";
	
	public static final String GET_ALL_FILES_ORIGINAL_SIZE = "SELECT COALESCE(SUM("+RESOURCE_ORIGINAL_SIZE+"),0) AS total_original_size FROM "+RESOURCE_TABLE+" WHERE "+USER_ID+" = ? AND "+RESOURCE_TYPE+" = ? ";
	
	public static final String GET_ALL_FILES_COMPRESSED_SIZE = "SELECT COALESCE(SUM(DISTINCT D."+DFS_FILE_SIZE+")) AS total_compress_size FROM "+DFS_TABLE_NAME+" D JOIN "+RESOURCE_TABLE+" R ON R."+RESOURCE_ID+" = D."+DFS_FILE_ID+" WHERE R."+USER_ID+"= ? ";
	
	public static final String GET_ALL_DEDUPLICATE_FILES ="SELECT COUNT(DISTINCT D." + DFS_PATH + ") AS unique_paths_count " +"FROM " + DFS_TABLE_NAME + " D " +"JOIN " + RESOURCE_TABLE + " R ON R." + RESOURCE_ID + " = D." + DFS_FILE_ID + " " +"WHERE R." + USER_ID + " = ? ";

	public static final String FIND_ALL_FILES ="SELECT COUNT(" + RESOURCE_NAME + ") AS total_files " +"FROM " + RESOURCE_TABLE + " " +"WHERE " + USER_ID + " = ? AND " + RESOURCE_TYPE + " = ? ";

	public static final String FILE_ORIGINAL_SIZE = "SELECT * FROM "+RESOURCE_TABLE+" WHERE "+RESOURCE_ID+" = ?";
	
	public static final String GET_USER_DETAILS = "SELECT * FROM "+USERS_TABLE+" WHERE "+USER_ID+" = ? ";
	
	public static final String UPDATE_PROFILE = "UPDATE "+USERS_TABLE+" SET "+USER_NAME+" = ?, "+USER_TIMEZONE+" = ? WHERE "+USER_ID+" = ?";
	
	public static final String UPDATE_PROFILE_WITH_PASSWORD="UPDATE "+USERS_TABLE+" SET "+USER_NAME+" = ?, "+USER_TIMEZONE+" = ?, "+USER_PASSWORD+" = ? WHERE "+USER_ID+" = ?";
	
	public static final String UPDATE_DFS_PATH = "UPDATE "+DFS_TABLE_NAME+" SET "+DFS_PATH+" = ? , "+FILE_CHECKSUM+" = ? , "+DFS_FILE_SIZE+"= ? WHERE "+DFS_FILE_ID+" = ?";
	
	public static final String GET_FILE_ID_USING_FILE_NAME = "SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ? AND "+RESOURCE_NAME+" = ?";
	
	public static final String GET_FILE_VERSIONS_SIZE = "SELECT SUM("+VERSION_SIZE+") As total_size FROM "+VERSION_TABLE_NAME+" WHERE "+VERSION_DFS_ID+" = ?";
	
	public static final String GET_DEDUPLICATE_FILES_SIZES = "SELECT D."+DFS_PATH+" , COUNT(*) AS count , (COUNT(*) -1) * MAX(D."+DFS_FILE_SIZE+") AS size FROM "+DFS_TABLE_NAME+" D JOIN "+RESOURCE_TABLE+" R ON R."+RESOURCE_ID+" = D."+DFS_FILE_ID+" WHERE R."+USER_ID+" = ? GROUP BY D."+DFS_PATH+" HAVING COUNT(*) > 1";
	
	public static final String UPDATE_FILE_STATUS = "UPDATE "+RESOURCE_TABLE+" SET "+RESOURCE_STATUS+" = ? WHERE "+RESOURCE_ID+" = ? OR "+PARENT_ID+" = ?";
	
	public static final String SELECT_ALL_TRASH_FILES = "SELECT * FROM "+RESOURCE_TABLE+" WHERE "+USER_ID+" = ? AND "+RESOURCE_STATUS+" = ?";
	
	public static final String TOTAL_TRASH_RESOURCES = "SELECT COUNT(*) AS count FROM "+RESOURCE_TABLE+" WHERE "+USER_ID+" = ? AND "+RESOURCE_STATUS+" = ?";
	
	public static final String FIND_PARTCULAR_RESOURCE_COUNT = "SELECT COUNT(*)AS count FROM "+RESOURCE_TABLE+" WHERE "+USER_ID+" = ? AND "+RESOURCE_STATUS+" = ? AND "+RESOURCE_TYPE+" = ?";
	
	public static final String GET_ALL_FILES_TRASH_SIZE = "SELECT SUM("+RESOURCE_ORIGINAL_SIZE+") AS size FROM "+RESOURCE_TABLE+" WHERE "+USER_ID+" = ? AND "+RESOURCE_STATUS+" = ?";
	
	public static final String CHECK_RESOURCES = "SELECT * FROM "+RESOURCE_TABLE+" WHERE "+RESOURCE_ID+" = ? AND "+RESOURCE_STATUS+" = ?";
	
	public static final String UPDATE_PARENT_ID = "UPDATE "+RESOURCE_TABLE+" SET "+PARENT_ID+" = ? WHERE "+RESOURCE_ID+" = ? AND "+USER_ID+" = ?";
	
	public static final String UPDATE_FOLDER_STATUS = "UPDATE "+RESOURCE_TABLE+" SET "+RESOURCE_STATUS+" = ? WHERE "+RESOURCE_ID+" = ?";
	
	public static final String UPDATE_FOLDER_FILES_STATUS = "UPDATE "+RESOURCE_TABLE+" SET "+RESOURCE_STATUS+" = ? WHERE "+PARENT_ID+" = ? AND "+RESOURCE_STATUS+" = ? ";
	
	public static final String SEARCH_RESOURCES="SELECT * FROM "+RESOURCE_TABLE+" WHERE "+USER_ID+"= ? AND ("+RESOURCE_NAME+" LIKE CONCAT(?,'%') OR "+RESOURCE_NAME+" LIKE CONCAT('%',?) OR "+RESOURCE_NAME+" LIKE CONCAT('%',?,'%'))"+" ORDER BY CASE WHEN "+RESOURCE_NAME+" LIKE CONCAT(?,'%') THEN 1 WHEN "+RESOURCE_NAME+" LIKE CONCAT('%',?) THEN 2 WHEN "+RESOURCE_NAME+" LIKE CONCAT('%',?,'%') THEN 3 END";
	
}

