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
package pearrewards.service;

import pearrewards.control.ServiceManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 *
 * @author antonio
 */
public class ListenerEvent implements Listener {
    
    private ServiceManager sm;
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String username = p.getName();
        
        if((sm.checkData(username) == 1) && (sm.getReedemRewards(username) == sm.getNumRewards(username))) {
            
            if(sm.getNumRewards(username) == sm.getDailyElements().size())
                sm.resetRewards(username);
            else
                sm.incrementNumRewards(username);
            
        } else if((sm.checkData(username) > 1) || (sm.getReedemRewards(username) != sm.getNumRewards(username))) {
            sm.resetRewards(username);
        }
        
        sm.updateDate(username);
        
        if(sm.getReedemRewards(username) != sm.getNumRewards(username))
            
        	p.spigot().sendMessage(new ComponentBuilder(sm.getConfig().getString("notify_message").replaceAll("&", "§"))
        			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aRiscatta reward!")))
        			.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/daily"))
        			.create()
        	);
        	
    }
    
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        
        if(e.getCurrentItem() == null)
            return;
        
        if(e.getView().getTitle().equals("§l§aPearRewards")) {
            if(e.getCurrentItem().getItemMeta().isUnbreakable()) {
                Player p = (Player)e.getWhoClicked();
                int giorno = sm.getNumRewards(p.getName()) - 1;
                
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), 
                        getGiveCommand(
                                sm.getDailyElements().get(giorno).getString("give_command"),
                                p.getName()
                        )
                );
                
                p.closeInventory();
                sm.updateReedemRewards(p.getName());
                
            } 
        
            e.setCancelled(true);
        }
    }
    
    private String getGiveCommand(String command, String username) {
        return command.replace("%p%", username);
    }
    
    public void setServiceManager(ServiceManager sm) {
        this.sm = sm;
    }
    
    
}
