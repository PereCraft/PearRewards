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
package pearrewards.control;

import pearrewards.PearRewards;
import pearrewards.connection.ConnectionDispatcher;
import pearrewards.connection.IConnection;
import pearrewards.persistence.ConfigurationFile;
import pearrewards.service.Commands;
import pearrewards.service.ListenerEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import pearrewards.domain.User;
import pearrewards.exception.PearRewardsException;


/**
 *
 * @author antonio
 */
public class ServiceManager {
    
    private static ServiceManager sm = null;
    
    private PearRewards plugin;
    private Commands commands;
    private ListenerEvent listener;
    private ConfigurationFile config;
    private IConnection connection;
    
    private ServiceManager(PearRewards plugin, Commands commands, ListenerEvent listener, ConfigurationFile config) throws ClassNotFoundException, SQLException, Exception {
        this.plugin     = plugin;
        this.commands   = commands;
        this.listener   = listener;
        this.config     = config;
        this.connection = ConnectionDispatcher.getDB(
                config
        );
        
        // commands init
        this.commands.setServiceManager(this);
        this.commands.setConfig(this.getConfig());
        this.commands.setElements(this.getDailyElements());
        
        // listener init
        this.listener.setServiceManager(this);
        
    }
    
    public static ServiceManager getServiceManager(PearRewards plugin, Commands commands, ListenerEvent listener, ConfigurationFile config) throws Exception, ClassNotFoundException, SQLException {
        if(sm == null) 
            sm = new ServiceManager(plugin, commands, listener, config);
        
        return sm;
    }
    
    // COMANDI ADMIN //
    
    public Commands getCommands() {
        return commands;
    }
    
    public ListenerEvent getListener() {
        return listener;
    }
    
    public FileConfiguration getConfig() {
        return config.getConfig();
    }
    
    public void restart() {
        plugin.onReload();
    }
    
    public boolean refreshConfig() {
        try {
            config.refreshConfig();
            commands.setConfig(this.getConfig());
            commands.setElements(this.getDailyElements());
        } catch(Exception ex) {
            return false;
        }
        
        return true;
    }
    
    public void disablePlugin() {
        this.plugin     = null;
        this.commands   = null;
        this.listener   = null;
        this.config     = null;
        this.connection = null;
        
        this.sm = null;
    }
    
    // COMANDI COMMANDS //
    
    public List<ConfigurationSection> getDailyElements() {
        return config.getDailyElements();
    }
    
    public void createUser(String username) throws PearRewardsException {
        connection.createUser(username);        
    }
    
    public User readUser(String username) throws PearRewardsException {
        return connection.readUser(username);
    }
    
    public void updateUser(User user) throws PearRewardsException {
        connection.updateUser(user);
    }
    
}
