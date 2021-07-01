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
package pearrewards.persistence;

import pearrewards.PearRewards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


/**
 *
 * @author antonio
 */
public class ConfigurationFile {
    
    private static ConfigurationFile config = null;
    
    private File file;
    private YamlConfiguration configFile;
    
    private ConfigurationFile() throws IOException {
        file = new File(PearRewards.getPlugin().getDataFolder().getPath() + "/config.yml");
                
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            
            System.out.println("[PearRewards] Il file di configurazione non esiste, creazione del file config.yml");
            InputStream in = PearRewards.class.getResourceAsStream("/config.yml");
            OutputStream out = new FileOutputStream(file);
            
            try { 
                int n; 

                while ((n = in.read()) != -1) { 
                    out.write(n); 
                } 
            } finally { 
                if (in != null) in.close();
                if (out != null) out.close(); 
            } 
            
        }
        
        configFile = YamlConfiguration.loadConfiguration(file);
                
    }
    
    public static ConfigurationFile getConfigFile() throws IOException {
        
        if(config == null)
            config = new ConfigurationFile();
        
        return config;
    }
    
    public void refreshConfig() {
        configFile = YamlConfiguration.loadConfiguration(file);
    }
    
    public FileConfiguration getConfig() {
        return configFile;
    }

    public List<ConfigurationSection> getDailyElements() {
        List<ConfigurationSection> list = new ArrayList<>();
        
        configFile.getConfigurationSection("rewards").getKeys(false).forEach((day) -> {
            list.add(configFile.getConfigurationSection("rewards."+day));
        });
        
        return list;
    }
    
}
