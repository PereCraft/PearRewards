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
import pearrewards.view.GuiDispatcher;
import pearrewards.view.IGui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 *
 * @author antonio
 */
public class Commands implements CommandExecutor {

    private FileConfiguration config;
    private ArrayList<ConfigurationSection> elements;
    private ServiceManager sm;
    
    public Commands() {}
    
    public void setServiceManager(ServiceManager sm) {
        this.sm = sm;
    }
    
    public void setConfig(FileConfiguration config) {
        this.config = config;
    }
    
    public void setElements(List<ConfigurationSection> elements) throws Exception {
        this.elements = (ArrayList<ConfigurationSection>)elements;
        
        if(elements.isEmpty())
            throw new Exception("[PereRewards] Error on the configuration file, check the rewards section");
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String name, String[] args) {
        
        if(cs instanceof Player) {
            Player p = (Player)cs;
            IGui gui = GuiDispatcher.getGUI(config, p, elements.size());
            
            if(cmd.getName().equalsIgnoreCase("daily") && p.hasPermission("pearrewards.shortdaily")) {
                
                if(args.length == 0) {                
                    if(sm.getNumRewards(p.getName()) == sm.getReedemRewards(p.getName())) {
                        gui.usedRewardMenu(config.getString("used_rewards"));
                        return false;
                    }else if(p.getInventory().firstEmpty() == -1) {
                        p.sendMessage(ChatColor.RED + config.getString("empty_message"));
                        return false;
                    }

                    gui.openMenu(String.valueOf(sm.getNumRewards(p.getName())));
                    return true;
                }
                
            } else if(cmd.getName().equalsIgnoreCase("pearrewards")) {                
                
                if((args.length == 0) || ((args.length == 1) && (args[0].equalsIgnoreCase("help")))) {
                    
                    p.sendMessage(net.md_5.bungee.api.ChatColor.GREEN + "----- PearRewards help page ------------");
                
                    p.spigot().sendMessage(new ComponentBuilder("/daily")
                                .color(net.md_5.bungee.api.ChatColor.YELLOW)
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/daily "))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Mostra la gui per riscattare i reward!")))
                                .create()
                    );

                    p.spigot().sendMessage(new ComponentBuilder("/pr (help)")
                                .color(net.md_5.bungee.api.ChatColor.YELLOW)
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pr help"))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Mostra questa schermata!")))
                                .create()
                    );

                    p.sendMessage(net.md_5.bungee.api.ChatColor.GREEN + "----- PearRewards help page -----");
                    
                    return true;
                } else if((args.length == 1) && p.hasPermission("pr.settingsrewards")) {

                    if(args[0].equalsIgnoreCase("restart")) {
                        sm.restart();
                        p.sendMessage("[PearRewards] Plugin restarted!");
                        return true;
                    } else if(args[0].equalsIgnoreCase("refresh")) {
                        boolean ref = sm.refreshConfig();
                        
                        if(ref) p.sendMessage("[PearRewards] config.yml reloaded");
                        else p.sendMessage("[PearRewards] There was an error!");
                        
                        return ref;
                    }
                    
                }
                                
            }
            
            return false;
            
        } else if(cs instanceof ConsoleCommandSender) {
            
            if(cmd.getName().equalsIgnoreCase("pearrewards")) {                
                
                if(args.length == 1) {
                    
                    if(args[0].equalsIgnoreCase("restart")) {
                        sm.restart();
                        cs.sendMessage("[PearRewards] Plugin restarted!");
                        return true;
                    } else if(args[0].equalsIgnoreCase("refresh")) {
                        boolean ref = sm.refreshConfig();
                        
                        if(ref) cs.sendMessage("[PearRewards] config.yml reloaded");
                        else cs.sendMessage("[PearRewards] There was an error!");
                        
                        return ref;
                    }
                    
                }
            }
        }
        
        return false;
    }

}
