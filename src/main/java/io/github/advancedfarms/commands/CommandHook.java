package io.github.advancedfarms.commands;

import io.github.advancedfarms.AdvancedFarms;
import io.github.advancedfarms.config.Config;
import io.github.advancedfarms.util.Extras;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static io.github.advancedfarms.util.Util.*;

public class CommandHook implements CommandExecutor {

    private final AdvancedFarms plugin;

    public CommandHook(AdvancedFarms plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            helpcmd(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
            case "rl":
                if (sender.hasPermission("farms.admin") || sender instanceof Player) {
                    handleReload(sender);
                } else {
                    nopermission(sender);
                }
                break;

            case "give":
                if (args.length == 4) {
                    if (sender instanceof Player && (sender.hasPermission("farms.admin") || sender.isOp())) {
                        handleGive((Player) sender, args);
                    } else {
                        nopermission(sender);
                    }
                } else {
                    usage(sender, "/farm give [player] [amount] [level]");
                }
                break;

            case "fragment":
                if (args.length == 2) {
                    if (sender instanceof Player && (sender.hasPermission("farms.admin") || sender.isOp())) {
                        handleFragment((Player) sender, args[1]);
                    } else {
                        nopermission(sender);
                    }
                } else {
                    usage(sender, "/farm fragment [player]");
                }
                break;

            case "help":
                helpcmd(sender);
                break;

            default:
                unknowncommand(sender);
                break;
        }

        return false;
    }

    private void handleReload(CommandSender sender) {
        plugin.configManager.reload("messages.yml");
        plugin.configManager.save("messages.yml");
        plugin.reloadConfig();
        String message = ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("reload"));
        message(sender, message);
    }

    private void handleGive(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);

        if(target == null) {
            message(player, ChatColor.RED + "Player username cannot be null.");
        }
        if (target != null) {
            try {
                int amount = Integer.parseInt(args[2]);
                int level = Integer.parseInt(args[3]);
                if (amount == 0 || level == 0) {
                    message(player, ChatColor.RED + "Invalid amount or level.");
                    return;
                }

                ItemStack harvesterHoe = Extras.harvesterHoe();
                ItemMeta itemMeta = harvesterHoe.getItemMeta();
                if (itemMeta == null) return;

                List<String> lore;
                if (itemMeta.getLore() != null) {
                    lore = new ArrayList<>(itemMeta.getLore());
                } else {
                    lore = new ArrayList<>();
                }
                while (lore.size() <= 4) {
                    lore.add("");
                }

                String sugarcaneNameColor = plugin.getMessages().getString("tool.tool_name_color");
                String sugarcaneMainColor = plugin.getMessages().getString("tool.tool_main_color");
                String sugarcaneSecondaryColor = plugin.getMessages().getString("tool.tool_secondary_color");
                String sugarcaneValueColor = plugin.getMessages().getString("tool.tool_value_color");


                for (int i = 0; i < lore.size(); i++) {
                    String line = lore.get(i);
                    if (line.contains("Cane Harvested:")) {
                        lore.set(i, ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Harvested: " + sugarcaneValueColor + Config.getLevelBreak(level)));
                    }
                }

                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', sugarcaneNameColor + "&lHarvester Hoe") + ChatColor.GRAY + " (Level " + level + ")");
                lore.set(4, ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Multiplier: " + sugarcaneValueColor + Config.getLevelMultiplier(level) + "x"));
                itemMeta.setLore(lore);
                harvesterHoe.setItemMeta(itemMeta);

                for (int i = 0; i < amount; i++) {
                    target.getInventory().addItem(harvesterHoe);
                }

                String message = plugin.getMessages().getString("given-harvester-hoe");
                message = message.replace("{player}", target.getName()).replace("{amount}", Integer.toString(amount));
                message(player, message);
                target.sendMessage(ChatColor.GREEN + "You have received " + amount + " Harvester Hoe(s)!");
            } catch (NumberFormatException e) {
                message(player, ChatColor.RED + "Invalid number format. Please check the input.");
            }
        } else {
            message(player, ChatColor.RED + "Player not found.");
        }
    }

    private void handleFragment(Player player, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target != null) {
            ItemStack fragment = Extras.caneFragment(player);
            target.getInventory().addItem(fragment);
            message(player, ChatColor.translateAlternateColorCodes('&', "You have given " + target.getName() + " a Fragment."));
            target.sendMessage(ChatColor.GREEN + "You have received a Fragment.");
        } else {
            message(player, ChatColor.RED + "Player not found.");
        }
    }
}
