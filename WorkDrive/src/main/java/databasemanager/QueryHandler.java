package databasemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryHandler {

	public static ResultSet executeQuerry(String querry, Object[] params) {

		Connection con = DBConnection.getInstance();

		try {
			PreparedStatement ps = con.prepareStatement(querry);

			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}

			ResultSet rs = ps.executeQuery();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static int executeUpdate(String querry, Object[] params) {
		Connection con = DBConnection.getInstance();

		try (PreparedStatement ps = con.prepareStatement(querry)) {
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}

			return ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}
}
