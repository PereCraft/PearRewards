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

import pearrewards.PearRewards;
import pearrewards.persistence.ConfigurationFile;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author antonio
 */
class ConnectionSQLite implements IConnection {
    
    private String url;
    private Connection connection = null;
    
    public ConnectionSQLite(ConfigurationFile config) throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        
        this.url = "jdbc:sqlite:" + PearRewards.getPlugin().getDataFolder().getPath() + 
                "/" + config.getConfig().getString("connect_db.sqlite.database");
        
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    @Override
    public void checkDBexist() throws SQLException {
        connection = (Connection)DriverManager.getConnection(url);
        
        String sql = "CREATE TABLE IF NOT EXISTS PearRewards(ID INTEGER NOT NULL, "
            + "Username VARCHAR(20) NOT NULL, Date DATE, NumRewards INTEGER NOT NULL, "
            + "ReedemRewards INTEGER NOT NULL, PRIMARY KEY (ID))";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeUpdate();
    }
    
    @Override
    public boolean checkUserExist(String username) throws SQLException{
        String sql = "SELECT ID FROM PearRewards WHERE Username = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        
        ResultSet res = stmt.executeQuery();
        return res.next();
    }
    
    @Override
    public void createUser(String username) throws SQLException {

        LocalDate date = LocalDate.now();

        String sql = "INSERT INTO PearRewards (Username, Date, NumRewards, ReedemRewards) VALUES (?, ?, 1, 0)";        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, date.format(DateTimeFormatter.ISO_LOCAL_DATE));

        stmt.executeUpdate();        
    }
    
    @Override
    public Date getUserDate(String username) throws SQLException {
        
        String sql = "SELECT Date FROM PearRewards WHERE Username = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);

        ResultSet res = stmt.executeQuery();
        if(res.next()) {
            return Date.valueOf(res.getString("Date"));
        } else {
            throw new SQLException();
        }
        
    }
    
    @Override
    public boolean updateDate(String username) {
        
        try {
            String sql = "UPDATE PearRewards SET Date = ? WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            stmt.setString(2, username);

            stmt.executeUpdate();
            return true;
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
            return false;
        }
        
    }
    
    @Override
    public int getNumRewards(String username) {
        
        try {
            
            String sql = "SELECT NumRewards FROM PearRewards WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            
            ResultSet res = stmt.executeQuery();
            
            if(res.next()) {
                return res.getInt("NumRewards");
            } else {
                System.err.println("[PearRewards] Error while taking NumRewards");
                return 1;
            }        
            
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
            return 1;
        }
        
    }
    
    @Override
    public void incrementNumRewards(String username) {
        
        try {
            String sql = "UPDATE PearRewards SET NumRewards = NumRewards+1 WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            
            stmt.executeUpdate();
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
        }
        
    }
    
    @Override
    public void resetNumRewards(String username) {
        
        try {
            String sql = "UPDATE PearRewards SET NumRewards = 1 WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            
            stmt.executeUpdate();
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
        }
        
    }
    
    @Override
    public void incrementReedemRewards(String username) {
        
        try {
            String sql = "UPDATE PearRewards SET ReedemRewards = ? WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, getNumRewards(username));
            stmt.setString(2, username);
            
            stmt.executeUpdate();
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
        }
        
    }
    
    @Override
    public void resetReedemRewards(String username) {
        
        try {
            String sql = "UPDATE PearRewards SET ReedemRewards = 0 WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            
            stmt.executeUpdate();
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
        }
        
    }
    
    @Override
    public int getReedemRewards(String username) {
        
        try { 
            String sql = "SELECT ReedemRewards FROM PearRewards WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            
            ResultSet res = stmt.executeQuery();
            if(res.next()) {
                return res.getInt("ReedemRewards");
            } else {
                System.err.println("[PearRewards] Error while taking NumRewards");
                return 1;
            }  
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
            return 1;
        }
        
    }
    
}
