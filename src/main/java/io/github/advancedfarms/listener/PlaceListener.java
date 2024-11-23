package io.github.advancedfarms.listener;

import io.github.advancedfarms.AdvancedFarms;
import io.github.advancedfarms.util.Extras;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PlaceListener implements Listener {

    private final AdvancedFarms plugin;
    public PlaceListener(AdvancedFarms plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack item = Extras.getItemInHand(player);

        if (item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null) {
            return;
        }

        if (item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
            e.setCancelled(true);
        }
    }
}

