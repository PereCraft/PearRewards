/*
 * Copyright (C) 2021 PereCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pearrewards.connection;

import pearrewards.persistence.ConfigurationFile;
import pearrewards.domain.User;
import pearrewards.exception.PearRewardsException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.DateFormat;

/**
 *
 * @author antonio
 */
class ConnectionMySQL implements IConnection {
    
    private String username;
    private String password;
    private String url;

    public ConnectionMySQL(ConfigurationFile config) throws PearRewardsException {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            this.username = config.getConfig().getString("connect_db.mysql.username");
            this.password = config.getConfig().getString("connect_db.mysql.password");
            this.url = "jdbc:mysql://localhost:3306/" + 
                    config.getConfig().getString("connect_db.mysql.database");
        } catch(ClassNotFoundException ex) {
            throw new PearRewardsException("[Error] PearRewards: Driver not found!");
        }
        
        checkTableExist();
    }
    
    // GETTER E SETTER //

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    // PRENDO TABELLA DB //
    
    private Connection openConnection() throws PearRewardsException {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch(SQLException ex) {
            throw new PearRewardsException("[Error] PearRewards: Can't open a connection: " + ex.getMessage());
        }
    }
    
    private void closeConnection(Connection conn) throws PearRewardsException { 
        try {
            conn.close();
        } catch(SQLException ex) {
            throw new PearRewardsException("[Error] PearRewards: Can't close a connection: " + ex.getMessage());
        }
    }
    
    private void checkTableExist() throws PearRewardsException {
        Connection conn = null;
        
        try {
            conn = DriverManager.getConnection(url, username, password);
            String sql = "CREATE TABLE IF NOT EXIST PearRewards(ID int NOT NULL AUTO_INCREMENT, " +
                    "Username varchar(20) NOT NULL, Date DATE, NumRewards int NOT NULL, " + 
                    "ReedemRewards int NOT NULL, PRIMARY KEY(ID);";
            conn.createStatement().execute(sql);
        } catch(SQLException ex) {
            throw new PearRewardsException("[Error] PearRewards: Can't create the table: " + ex.getMessage());
        } finally {
            closeConnection(conn);
        }
    }
    
    public void createUser(String username) throws PearRewardsException { // Aggiungere eccezione personalizzata
        Connection conn = null;
        
        try {
            conn = openConnection();
        } catch(PearRewardsException ex) {
            throw new PearRewardsException(ex.getMessage());
        }

        String sql = "INSERT INTO PearRewards (Username, Date, NumRewards, ReedemRewards) VALUES (?, ?, 1, 0)";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            
            conn.createStatement().execute(sql);
        } catch(SQLException ex) {
            throw new PearRewardsException("[Error] PearRewards: Can't insert the user: " + ex.getMessage());
        } finally {
            closeConnection(conn);
        }

    }
    
    public User readUser(String username) throws PearRewardsException {
        Connection conn = null;
        
        try {
            String sql = "SELECT * FROM PearRewards WHERE Username = " + username;
            conn = openConnection();
            
            ResultSet result = conn.createStatement().executeQuery(sql);
            
            result.first();
            if(result.getRow() == 0) throw new PearRewardsException("[Error] PearRewards: Can't get information about the user!");
            
            return new User(
                    username,
                    result.getDate("date"),
                    result.getInt("NumRewards"),
                    result.getInt("ReedemRewards")
            );
        } catch(SQLException ex) {
            throw new PearRewardsException("[Error] PearRewards: Error with SQL syntax: " + ex.getMessage());
        } finally {
            closeConnection(conn);
        }
    }
    
    public void updateUser(User user) throws PearRewardsException {
        Connection conn = null;
        
        try {
            String sql = "UPDATE PearRewards Date = '" + DateFormat.getDateInstance().format(user.getDate()) + "', " + 
                    "NumRewards = " + user.getNumRewards() + ", ReedemRewards = " + user.getReedemRewards() + 
                    " WHERE Username = " + user.getUsername() + ";";
            
            conn = openConnection();
            if(conn.createStatement().executeUpdate(sql) != 1) throw new PearRewardsException("[Error] PearRewards: Can't update information about the user!");
        } catch(SQLException ex) {
            throw new PearRewardsException("[Error] PearRewards: Error with SQL syntax: " + ex.getMessage());
        } finally {
            closeConnection(conn);
        }
    }
}