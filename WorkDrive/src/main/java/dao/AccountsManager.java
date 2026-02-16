package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import constants.Queries;
import databasemanager.QueryHandler;
import hashing.AESEncryption;
import hashing.PasswordHashing;
import utils.SnowflakeIdGenerator;

public class AccountsManager {

	public static boolean isDuplicateUser(String email) throws SQLException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.CHECK_DUPLICATE_USER, new Object[] { email });
		
		return rs.next();
	}

	public static boolean addUser(String email, String fullName, String password, String timeZone) throws Exception {
		String encryptPass;
		long id = SnowflakeIdGenerator.nextId();
		encryptPass = new PasswordHashing().passwordHashing(password);
		QueryHandler.executeUpdate(Queries.ADD_USER, new Object[] { id, email, fullName, encryptPass, timeZone });
		return true;
	}

	public static boolean validateUser(String email, String password) throws Exception {
		String encryptPass;
		encryptPass = new PasswordHashing().passwordHashing(password);

		ResultSet rs = QueryHandler.executeQuerry(Queries.VALIDATE_USER, new Object[] { email, encryptPass });
		rs.next();
		return rs.getInt(1) > 0;

	}

	public static long getUserId(String email) throws Exception {
		String decryptEmail=new AESEncryption().decrypt(email);
		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_USERID, new Object[] { decryptEmail });
		long userId = 0;
		if(rs.next()) {
			userId = rs.getLong("UserId");
		}
		return userId;
	}
	
	public static JSONObject getUserDetails(long userId) throws JSONException, SQLException {
		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_USER_DETAILS, new Object[] { userId });
		
		if (rs.next()) {
			JSONObject responseObject = new JSONObject();
			responseObject.put("FullName", rs.getString("FullName"));
			responseObject.put("Email", rs.getString("Email"));
			responseObject.put("TimeZone", rs.getString("TimeZone"));
			
			return responseObject;
		}
		return null;
	}
	
	public static boolean updateUser(String name,String timeZone,String encryptedPassword,long userId) {
		int result;

		if (encryptedPassword != null) {

			result = QueryHandler.executeUpdate(Queries.UPDATE_PROFILE_WITH_PASSWORD,
					new Object[] { name, timeZone, encryptedPassword, userId });

		} else {

			result = QueryHandler.executeUpdate(Queries.UPDATE_PROFILE,new Object[] {name, timeZone, userId});
		}
		
		return result>0;
	}

}
