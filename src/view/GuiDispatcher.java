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
package pearrewards.view;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author antonio
 */
public class GuiDispatcher {
        
    public static IGui getGUI(FileConfiguration config, Player p, int slots) {
        
        String type = config.getString("guy_type");
        
        switch(type) {
            case "chest":
                System.out.println("[PearRewards] Using chest gui.");
                return new GuiChest(p, slots);
            case "daily":
                System.out.println("[PearRewards] Using daily gui.");
                return new GuiDaily(p, slots);
            default:
                System.err.println("[PearRewards] The type \"" + type + "\" it's invalid. Check the file config.yml");
                System.err.println("[PearRewards] Using chest gui.");
                return new GuiChest(p, slots);
        }
        
    }

    
}
