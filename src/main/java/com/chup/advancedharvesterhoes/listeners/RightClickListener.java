package com.chup.advancedharvesterhoes.listeners;

import com.chup.advancedharvesterhoes.Extras;
import com.chup.advancedharvesterhoes.Main;
import com.chup.advancedharvesterhoes.UpgradeGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RightClickListener implements Listener {

    private Main plugin;

    public RightClickListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRightClick(PlayerInteractEvent e) {

        String sugarcaneNameColor;
        if(plugin.getMessages().contains("tool")) {
            sugarcaneNameColor = plugin.getMessages().getString("tool.tool_name_color");
        } else {
            sugarcaneNameColor = "&a";
        }

        Action action = e.getAction();
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = e.getPlayer();
            ItemStack hand = Extras.getItemInHand(player);
            if (hand != null && hand.getItemMeta() != null && hand.getItemMeta().getDisplayName() != null &&
                    hand.getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', sugarcaneNameColor + "&lHarvester Hoe"))) {
                e.setCancelled(true);
                Inventory inv = new UpgradeGUI(this.plugin).openInventory(player, hand);
                player.openInventory(inv);
            }
        }
    }
}
