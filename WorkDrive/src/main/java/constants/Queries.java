package constants;

import static constants.ColumnNames.*;

public class Queries {


	public static final String GET_USERID="SELECT "+USER_ID+" FROM "+USERS_TABLE+" WHERE "+USER_EMAIL+" = ?";
	
	public static final String CHECK_DUPLICATE_USER = "SELECT "+USER_EMAIL+" FROM "+USERS_TABLE+" WHERE "+USER_EMAIL+"=?";
	
	public static final String ADD_USER = "INSERT INTO "+USERS_TABLE+" ("+USER_ID+", "+USER_EMAIL+", FullName, "+USER_PASSWORD+", "+USER_TIMEZONE+") VALUES (?, ?, ?, ?, ?)";
	
	public static final String VALIDATE_USER = "SELECT COUNT(*) FROM "+USERS_TABLE+" WHERE "+USER_EMAIL+" = ? AND "+USER_PASSWORD+" = ?";

	public static final String GET_RESOURCES="SELECT r.*, u."+USER_TIMEZONE+" FROM "+RESOURCE_TABLE+" r JOIN Users u ON r.UserId = u.UserId WHERE r."+USER_ID+" = ? AND r."+PARENT_ID+" = ?";
	
	public static final String GET_ROOT_ID= "SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" IS NULL AND "+USER_ID+" = ?";
	
	public static final String GET_PARENT_ID = "SELECT "+PARENT_ID+" FROM "+RESOURCE_TABLE+" WHERE "+RESOURCE_ID+" = ? ;";
	
	public static final String DELETE_RESOURCE = "DELETE FROM "+RESOURCE_TABLE+" WHERE "+RESOURCE_ID+" = ?;";
		
	public static final String ADD_RESOURCE = "INSERT INTO "+RESOURCE_TABLE+" ("+RESOURCE_ID+", "+RESOURCE_NAME+", "+PARENT_ID+", "+USER_ID+", "+CREATED_TIME+", "+MODIFIED_TIME+") VALUES (?,?,?,?,?,?)";
		
	public static final String ADD_RESOURCE_ROOT = "INSERT INTO "+RESOURCE_TABLE+" ("+RESOURCE_ID+", "+RESOURCE_NAME+", "+USER_ID+", "+CREATED_TIME+", "+MODIFIED_TIME+") VALUES (?,?,?,?,?)";
	
	public static final String UPDATE_RESOURCE="UPDATE "+RESOURCE_TABLE+" SET "+RESOURCE_NAME+" = ?, "+MODIFIED_TIME+" = ? WHERE "+RESOURCE_ID+" = ?";
	
	public static final String GET_RESOURCE="SELECT r.*, u."+USER_TIMEZONE+" FROM "+RESOURCE_TABLE+" r JOIN Users u ON r.UserId = u.UserId WHERE r."+RESOURCE_ID+" = ?";
	
	public static final String GET_ALL_FOLDER="SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ?";
	
	public static final String ADD_NEW_FILE = "INSERT INTO "+FILES_TABLE+" VALUES ( ? , ? , ? , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , ?)";
	
	public static final String UPDATE_FILENAME = "UPDATE "+FILES_TABLE+" SET "+FILE_NAME+" = ? , "+FILE_EDIT+" = CURRENT_TIMESTAMP WHERE "+FILE_ID+" = ? ";
	
	public static final String DELETE_FILE = "DELETE FROM "+FILES_TABLE+" WHERE "+FILE_NAME+" = ? and "+FILE_PAREND_ID+" = ?"; // my update
	
	public static final String DUPLICATE_FILE_CHECK = "SELECT * FROM "+FILES_TABLE+" where "+FILE_NAME+" = ? and "+FILE_PAREND_ID+" = ?"; // my update
	
	public static final String SHOW_ALL_FILES = "SELECT * FROM "+FILES_TABLE+" WHERE "+FILE_PAREND_ID+" = ?"; // my update
		
	public static final String UPDATE_PARENT = "UPDATE "+RESOURCE_TABLE+" SET "+PARENT_ID+" = ?,"+RESOURCE_NAME+" = ? WHERE "+RESOURCE_ID+" = ?";
	
	public static final String EXIST_NAME = "SELECT * FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ? AND "+RESOURCE_NAME+" = ? AND "+USER_ID+"= ? ;";
	
	public static final String GET_FILE_ID = "SELECT * FROM "+FILES_TABLE+" WHERE "+FILE_PAREND_ID+" = ? AND "+FILE_NAME+" = ?";
	
	public static final String GET_ALL_CONTAINS = "SELECT (SELECT COUNT(*) FROM "+FILES_TABLE+" WHERE "+FILE_PAREND_ID+" = ?) AS totalFiles, (SELECT COUNT(*) FROM "+RESOURCE_TABLE+" WHERE "+PARENT_ID+" = ?) AS totalFolders "; 
	
}
