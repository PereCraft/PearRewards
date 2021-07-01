/*
 * Copyright (C) 2021 antonio
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

/**
 *
 * @author antonio
 */
public class ConnectionDispatcher {
    
    public static IConnection getDB(ConfigurationFile config) throws ClassNotFoundException {
        
        String type = config.getConfig().getString("db_type");
        
        switch(type){
            case "mysql":
                System.out.println("[PearRewards] Creating mysql database.");
                return new ConnectionMySQL(config);
            case "sqlite":
                System.out.println("[PearRewards] Creating sqlite database.");
                return new ConnectionSQLite(config);
            default:
                System.err.println("[PearRewards] The type \"" + type + "\" it's invalid. Check the file config.yml");
                System.err.println("[PearRewards] Creating sqlite database.");
                return new ConnectionSQLite(config);
        }
        
    }
    
}
