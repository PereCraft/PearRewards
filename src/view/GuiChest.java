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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author antonio
 */
class GuiChest implements IGui {
 
    private int slots;
    private Player p;
    
    public GuiChest(Player p, int slots) {
        this.p = p;
        this.slots = 27;
    }
         
    @Override
    public void openMenu(String numRewards) {        
        Inventory inv = Bukkit.createInventory(null, slots, "§l§aPearRewards");

        for(int i = 0; i < slots; i++) 
            inv.setItem(i, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));

        inv.setItem(slots/2, setReward("§l§a Giorno n. " + numRewards));
      
        p.openInventory(inv);
    }
    
    private ItemStack setReward(String itemName) {
        
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta  = item.getItemMeta();
        
        meta.setDisplayName(itemName);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);        
        item.setItemMeta(meta);
        
        return item;
    }

    @Override
    public void usedRewardMenu(String message) {
        ItemStack errorItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta meta = errorItem.getItemMeta();        
        meta.setDisplayName("§l§c" + message);
        errorItem.setItemMeta(meta);        
        
        Inventory inv = Bukkit.createInventory(null, slots, "§l§aPearRewards");
        
        for(int i = 0; i < slots; i++) 
            inv.setItem(i, errorItem);
        
        p.openInventory(inv);
    }
        
}