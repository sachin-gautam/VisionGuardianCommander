package application;

import java.sql.*;

public class DatabaseAdminLogin {
	
	private String username;
	private String password;
	
	private final String databaseName = "VisionGuardianCommander";
    private final String databaseUserName = "root";
    private final String databasePassword = "";
    
	private Connection conn;
	
	
	public void init() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, databaseUserName,
                    databasePassword);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public DatabaseAdminLogin getUserLoginDetails(String username, String password) {
        DatabaseAdminLogin userTemp = new DatabaseAdminLogin();

        String sql = "SELECT * FROM user_login_details WHERE username = ? and password = ? LIMIT 1";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                userTemp.setUsername(rs.getString("username"));
                userTemp.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userTemp;
    }

	 public void insertUser(String username, String password) {

			String sql = "INSERT INTO user_login_details(username, password) VALUES (?, ?)";
			
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
			  statement.setString(1, username);
			  statement.setString(2, password);
			
			  int rowsInserted = statement.executeUpdate();
			  if (rowsInserted > 0) {
			      System.out.println("New User was inserted successfully!");
			  }
			} catch (SQLException e) {
			  e.printStackTrace();
			}
	 }
	
	 public void db_close() {
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username; 
	}
	
	public String getPassword() {
		return password;
		
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

}
