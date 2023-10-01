package application;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;



class Database {
    public String specialCode;

    public String firstName;
    public String lastName;
    public int registrationId;
    public int ageInfo;
    public String sexInfo;
    public String photoDirectoryPath;
    public Boolean activeStatus;

    private final String databaseName = "VisionGuardianCommander";
    private final String databaseUserName = "root";
    private final String databasePassword = "";
    private Connection con;

    public boolean init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, databaseUserName,
                    databasePassword);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insert(String specialCode, String firstName, String lastName, int registrationId,
                       int ageInfo, String sexInfo, String photoDirectoryPath, Boolean activeStatus ) {

        String sql = "INSERT INTO userDatabase(special_code, first_name, last_name, registration_id, age_info, sex_info, photo_directory_path, active_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, specialCode);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setInt(4, registrationId);
            statement.setInt(5, ageInfo);
            statement.setString(6, sexInfo);
            statement.setString(7, photoDirectoryPath);
            statement.setBoolean(8, activeStatus);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Face data was inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Boolean update(String specialCode, String firstName, String lastName, int registrationId,
            int ageInfo, String sexInfo, String photoDirectoryPath, Boolean activeStatus, String altSpecialCode) {

				String sql = "UPDATE userDatabase SET special_code=?, first_name=?, last_name=?, registration_id=?, age_info=?, sex_info=?, photo_directory_path=?, active_status=? WHERE special_code=?";
				
				try (PreparedStatement statement = con.prepareStatement(sql)) {
				 statement.setString(1, specialCode);
				 statement.setString(2, firstName);
				 statement.setString(3, lastName);
				 statement.setInt(4, registrationId);
				 statement.setInt(5, ageInfo);
				 statement.setString(6, sexInfo);
				 statement.setString(7, photoDirectoryPath);
				 statement.setBoolean(8, activeStatus);
				 statement.setString(9, altSpecialCode);
				 //statement.setInt(9, userId);
				
				 int rowsUpdated = statement.executeUpdate();
				 if (rowsUpdated > 0) {
				     System.out.println("Face data was updated successfully!");
				 } else {
				     System.out.println("No records were updated.");
				     return false;
				 }
				} 
			catch (SQLException e) {
				 e.printStackTrace();
			}
				return true;
    }
    
    

    public ArrayList<String> getUser(String specialCode) {
        ArrayList<String> user = new ArrayList<>();

        String sql = "SELECT * FROM userDatabase WHERE special_code = ? LIMIT 1";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, specialCode);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                user.add(rs.getString("first_name"));
                user.add(rs.getString("last_name"));
                user.add(rs.getString("registration_id"));
                user.add(rs.getString("age_info"));
                user.add(rs.getString("sex_info"));
                user.add(rs.getString("photo_directory_path"));
                user.add(rs.getString("active_status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    
    public Database getUserAsDatabase(String specialCode) {
        Database userTemp = new Database();

        String sql = "SELECT * FROM userDatabase WHERE special_code = ? AND active_status = 1 LIMIT 1";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, specialCode);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                userTemp.setSpecialCode(rs.getString("special_code"));
                userTemp.setFirstName(rs.getString("first_name"));
                userTemp.setLastName(rs.getString("last_name"));
                userTemp.setRegistrationId(rs.getInt("registration_id"));
                userTemp.setAgeInfo(rs.getInt("age_info"));
                userTemp.setSexInfo(rs.getString("sex_info"));
                userTemp.setPhotoDirectoryPath(rs.getString("photo_directory_path"));
                userTemp.setActiveStatus(rs.getBoolean("active_status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userTemp;
    }
    
    
    public List<Database> getUsers() {
    	
    	
        List<Database> users = new ArrayList<>();

        String sql = "SELECT * FROM userDatabase;";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Database user = new Database();
                user.setSpecialCode(rs.getString("special_code"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setRegistrationId(rs.getInt("registration_id"));
                user.setAgeInfo(rs.getInt("age_info"));
                user.setSexInfo(rs.getString("sex_info"));
                user.setPhotoDirectoryPath(rs.getString("photo_directory_path"));
                user.setActiveStatus(rs.getBoolean("active_status"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String checkUserCode(String specialCode) {
        String userCode = new String();

        String sql = "SELECT * FROM userDatabase WHERE special_code = ? LIMIT 1";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, specialCode);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                userCode = rs.getString("special_code");
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userCode;
    }

    public void db_close() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getSpecialCode() {
        return specialCode;
    }

    public void setSpecialCode(String specialCode) {
        this.specialCode = specialCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public int getAgeInfo() {
        return ageInfo;
    }

    public void setAgeInfo(int age) {
        this.ageInfo = age;
    }

    public String getSexInfo() {
        return sexInfo;
    }

    public void setSexInfo(String sex) {
        this.sexInfo = sex;
    }
    
    public String getPhotoDirectoryPath() {
        return photoDirectoryPath;
    }

    public void setPhotoDirectoryPath(String photoDirectoryPath) {
        this.photoDirectoryPath = photoDirectoryPath;
    }
    
    public Boolean getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

}

