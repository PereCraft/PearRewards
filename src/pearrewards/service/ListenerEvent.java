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

import java.time.LocalDate;
import java.time.Period;
import pearrewards.PearRewards;
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
import pearrewards.domain.User;
import pearrewards.exception.PearRewardsException;

/**
 *
 * @author antonio
 */
public class ListenerEvent implements Listener {
    
    private ServiceManager sm;
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        
        Bukkit.getScheduler().runTaskLater(PearRewards.getPlugin(), () -> {
            
            Player p = e.getPlayer();
            String username = p.getName();
            
            try {
                User user = sm.readUser(username);
                int period = Period.between(LocalDate.parse(user.getDate().toString()), LocalDate.now()).getDays();
                
                if((period == 1) && (user.getReedemRewards() == user.getNumRewards())) {
                    if(user.getNumRewards() == sm.getDailyElements().size()) {
                        user.setReedemRewards(0);
                        user.setNumRewards(1);
                        
                        sm.updateUser(user);
                    } else {
                        user.setNumRewards(user.getNumRewards()+1);
                        sm.updateUser(user);
                    }
                } else if((period > 1) || (user.getReedemRewards() != user.getNumRewards())) {
                    user.setReedemRewards(0);
                    user.setNumRewards(1);
                        
                    sm.updateUser(user);
                }
                
                if(user.getReedemRewards() != user.getNumRewards()) {
                    p.spigot().sendMessage(new ComponentBuilder(sm.getConfig().getString("notify_message").replaceAll("&", "§"))
        			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aRiscatta reward!")))
        			.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/daily"))
        			.create()
                    );
                }
                
            } catch(PearRewardsException ex) {
                System.out.println("sas");
            }
            
        }, 40);        	
    }
    
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        
        if(e.getCurrentItem() == null) 
            return;
        
        if(e.getView().getTitle().equals("§l§aPearRewards")) {
            if(e.getCurrentItem().getItemMeta().isUnbreakable()) { 
                String username = ((Player)e.getWhoClicked()).getName();
                
                //TODO: Continuare
            }
        }
        
//        if(e.getCurrentItem() == null)
//            return;
//        
//        if(e.getView().getTitle().equals("§l§aPearRewards")) {
//            if(e.getCurrentItem().getItemMeta().isUnbreakable()) {
//                Player p = (Player)e.getWhoClicked();
//                int giorno = sm.getNumRewards(p.getName()) - 1;
//                
//                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), 
//                        getGiveCommand(
//                                sm.getDailyElements().get(giorno).getString("give_command"),
//                                p.getName()
//                        )
//                );
//                
//                p.closeInventory();
//                sm.updateReedemRewards(p.getName());
//                
//            } 
//        
//            e.setCancelled(true);
//        }
    }
    
    private String getGiveCommand(String command, String username) {
        return command.replace("%p%", username);
    }
    
    public void setServiceManager(ServiceManager sm) {
        this.sm = sm;
    }
    
    
}
