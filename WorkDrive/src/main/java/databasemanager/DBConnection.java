package databasemanager;



import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.SQLException;



public class DBConnection {



private static DBConnection instance;



private Connection con;



private DBConnection() {

try {

Class.forName("com.mysql.cj.jdbc.Driver");

con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Workdrive", "root","@Kr!$hn@1808");

} catch (SQLException | ClassNotFoundException e) {

// TODO Auto-generated catch block

e.printStackTrace();

}

}



	public static Connection getInstance() {
		if (instance == null) {
			
			instance = new DBConnection();
	
		}
	
		return instance.getConnection();
	}



public Connection getConnection() {

return con;

}



}
