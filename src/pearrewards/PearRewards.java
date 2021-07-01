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
package pearrewards;

import pearrewards.control.ServiceManager;
import pearrewards.persistence.ConfigurationFile;
import pearrewards.service.Commands;
import pearrewards.service.ListenerEvent;

import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author antonio
 */
public class PearRewards extends JavaPlugin {

    private static PearRewards plugin;
    private ServiceManager sm;
    
    @Override
    public void onEnable() {        
        plugin = this;
        
        try {
            sm = ServiceManager.getServiceManager(
                    plugin,
                    new Commands(),
                    new ListenerEvent(),
                    ConfigurationFile.getConfigFile()
            );
            
            getCommand("pearrewards").setExecutor(sm.getCommands());
            getCommand("daily").setExecutor(sm.getCommands());
            Bukkit.getPluginManager().registerEvents(sm.getListener(), plugin);
            
            // elements
            System.out.println("[PearRewards] Setted " + sm.getDailyElements().size() + " items as rewards:");
            for(int i = 0; i < sm.getDailyElements().size(); i++) {
                System.out.println("[" + (i+1) + "] " +
                        sm.getDailyElements().get(i).getString("give_command"));
            }
                
        } catch (IOException|ClassNotFoundException|SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
            onDisable();
        } catch (Exception ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
            onDisable();
        }
        
    }

    @Override
    public void onDisable() {
        sm.disablePlugin();  
        sm = null;
    }
    
    public void onReload() {
        System.out.println("[PearRewards] Reload plugin...");
        sm.refreshConfig();
        plugin.getServer().getPluginManager().disablePlugin(plugin);
        plugin.getServer().getPluginManager().enablePlugin(plugin);
    }
        
    public static PearRewards getPlugin() {
        return plugin;
    }
    
}
