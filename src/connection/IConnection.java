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

import java.sql.Date;
import java.sql.SQLException;

/**
 *
 * @author antonio
 */
public interface IConnection {
    
    /**
     * Check if database exists, and if not, create it.
     * @throws SQLException when it's impossible to connect to the database.
     */
    public void checkDBexist() throws SQLException;
    
    /**
     * Check if the player exists on the database.
     * @param username of the player to looking for.
     * @return true if the user was found, else return false.
     * @throws SQLException when it's impossible to connect to the database.
     */
    public boolean checkUserExist(String username) throws SQLException;
    
    /**
     * Create a record with the information about the player.
     * @param username of the player to insert on the database.
     * @throws SQLException when it's impossible to connect to the database.
     */
    public void createUser(String username) throws SQLException;
    
    /**
     * Get the date of the last time the player logged on.
     * @param username of the player to looking for.
     * @return the object Date found.
     * @throws SQLException when it's impossible to connect to the database, or 
     * it's impossible to get Date.
     */
    public Date getUserDate(String username) throws SQLException;
    
    /**
     * Update the date of a player.
     * @param username of the player to update the date
     * @return true if the update was succesful, else return false when it was
     * impossible to update the date of the player.
     */    
    public boolean updateDate(String username);
    
    /**
     * Get the number of rewards that he had to claim.
     * @param username of the player to get the number of rewards.
     * @return the number of rewards that he had to claim, if there's any error
     * return 1
     */
    public int getNumRewards(String username);
    
    /**
     * Increment of 1 the number of rewards
     * @param username of the player to increment the number of rewards.
     */
    public void incrementNumRewards(String username);
    
    /**
     * Set to 1 the number of rewards.
     * @param username of the player to reset the number of rewards.
     */
    public void resetNumRewards(String username);
    
    /**
     * Increment of 1 the number of reedem rewards.
     * @param username 
     */
    public void incrementReedemRewards(String username);
    
    /**
     * Set to 0 the number of reedem rewards.
     * @param username of the player to reset the number of reedem rewards.
     */
    public void resetReedemRewards(String username);
    
    /**
     * Get the number of rewards that he had claimed.
     * @param username of the player to get the number of reedem rewards.
     * @return the number of rewards that he had claimed, if there's any error
     * return 1
     */
    public int getReedemRewards(String username);
    
}
