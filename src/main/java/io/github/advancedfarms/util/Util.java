package io.github.advancedfarms.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Util {
    public static String getDividerAqua() {
        return ChatColor.BOLD + " " + ChatColor.AQUA + "-----------------------------------------------------";
    }

    public static void message(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.GREEN + "AdvancedHarvesterHoes >> " + ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void nopermission(CommandSender sender) {
        message(sender, ChatColor.RED + "You do not have permission to use this command.");
    }

    public static void usage(CommandSender sender, String usage) {
        message(sender, ChatColor.RED + "Usage: " + usage);
    }

    public static void unknowncommand(CommandSender sender) {
        message(sender, ChatColor.RED + "Unknown command. Type /farm help for a list of commands.");
    }

    public static void helpcmd(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(getDividerAqua());
            player.sendMessage(ChatColor.GOLD + "/farm reload" + ChatColor.WHITE + " - Reload the configuration.");
            player.sendMessage(ChatColor.GOLD + "/farm give <player> <amount> <level>" + ChatColor.WHITE + " - Give a Harvester Hoe.");
            player.sendMessage(ChatColor.GOLD + "/farm fragment <player>" + ChatColor.WHITE + " - Give a Fragment.");
            player.sendMessage(ChatColor.GOLD + "/farm help" + ChatColor.WHITE + " - Show this help message.");
            player.sendMessage(getDividerAqua());
        } else {
            Bukkit.getConsoleSender().sendMessage(getDividerAqua());
            Bukkit.getConsoleSender().sendMessage("/farm reload - Reload the configuration.");
            Bukkit.getConsoleSender().sendMessage("/farm give <player> <amount> <level> - Give a Harvester Hoe.");
            Bukkit.getConsoleSender().sendMessage("/farm fragment <player> - Give a Fragment.");
            Bukkit.getConsoleSender().sendMessage("/farm help - Show this help message.");
        }
    }
}