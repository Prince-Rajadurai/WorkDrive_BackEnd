package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

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

	public static boolean addUser(String email, String fullName, String password) throws Exception {
		String encryptPass;
		long id = SnowflakeIdGenerator.nextId();
		encryptPass = new PasswordHashing().passwordHashing(password);
		QueryHandler.executeUpdate(Queries.ADD_USER, new Object[] { id, email, fullName, encryptPass });
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
		String decryptEmail = new AESEncryption().decrypt(email);
		ResultSet rs = QueryHandler.executeQuerry(Queries.GET_USERID, new Object[] { decryptEmail });
		rs.next();
		return rs.getLong("UserId");
	}

}
