package com.chup.advancedharvesterhoes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HarvesterHoeExecutor implements CommandExecutor {

    private final Main plugin;

    public HarvesterHoeExecutor(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = ChatColor.GREEN.toString() + ChatColor.BOLD + "AdvancedHarvesterHoes " + ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + ">> ";
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp() || player.hasPermission("advancedharvesterhoes.admin")) {
                if (args.length == 1 && (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl"))) {
                    plugin.configManager.reload("messages.yml");
                    plugin.configManager.save("messages.yml");
                    plugin.reloadConfig();
                    String message = ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("reload"));
                    player.sendMessage(prefix + message);
                } else if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        try {
                            if (args[2] != null) {
                                int level = Integer.parseInt(args[3]);
                                if (Config.getLevelBreak(level) > 0 || level == 1) {
                                    int amount = Integer.parseInt(args[2]);
                                    ItemStack harvesterHoe = Extras.harvesterHoe();
                                    ItemMeta itemMeta = harvesterHoe.getItemMeta();
                                    List<String> updatedLore = new ArrayList<String>();
                                    String sugarcaneNameColor = plugin.getMessages().getString("tool.tool_name_color");
                                    String sugarcaneMainColor = plugin.getMessages().getString("tool.tool_main_color");
                                    String sugarcaneSecondaryColor = plugin.getMessages().getString("tool.tool_secondary_color");
                                    String sugarcaneValueColor = plugin.getMessages().getString("tool.tool_value_color");
                                    for (String line : harvesterHoe.getItemMeta().getLore()) {
                                        if (line.contains("Cane Harvested:")) {
                                            updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Harvested: " + sugarcaneValueColor + Config.getLevelBreak(level)));
                                        } else {
                                            updatedLore.add(line);
                                        }
                                    }
                                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', sugarcaneNameColor + "&lHarvester Hoe") + ChatColor.GRAY + " (Level " + level + ")");
                                    updatedLore.set(4, ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Multiplier: " + sugarcaneValueColor + Config.getLevelMultiplier(level) + "x"));
                                    itemMeta.setLore(updatedLore);
                                    harvesterHoe.setItemMeta(itemMeta);
                                    for (int i = 0; i < amount; i++) {
                                        target.getInventory().addItem(harvesterHoe);
                                    }
                                    String message = plugin.getMessages().getString("given-harvester-hoe");
                                    message = message.replace("{player}", target.getName());
                                    message = message.replace("{amount}", Integer.toString(amount));
                                    player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', message));
                                } else {
                                    String message = plugin.getMessages().getString("invalid-level");
                                    player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', message));
                                }
                            }
                        } catch (NumberFormatException e) {
                            String message = plugin.getMessages().getString("invalid-number");
                            player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', message));
                        }

                    } else {
                        String message = plugin.getMessages().getString("invalid-user");
                        player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', message));
                    }
                } else if (args.length == 2 && args[0].equalsIgnoreCase("fragment")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        ItemStack fragment = Extras.caneFragment();
                        target.getInventory().addItem(fragment);
                        String message = plugin.getMessages().getString("given-cane-fragment");
                        message = message.replace("{player}", target.getName());
                        player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', message));
                    } else {
                        String message = plugin.getMessages().getString("invalid-user");
                        player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', message));
                    }

                } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                    Iterator helpIterator = plugin.getMessages().getStringList("help").iterator();
                    while (helpIterator.hasNext()) {
                        String helpMessage = (String) helpIterator.next();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMessage));
                    }
                } else {
                    Iterator helpIterator = plugin.getMessages().getStringList("help").iterator();
                    while (helpIterator.hasNext()) {
                        String helpMessage = (String) helpIterator.next();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMessage));
                    }
                }
            } else {
                String message = plugin.getMessages().getString("no-permission");
                player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            if (args.length == 1 && (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl"))) {
                plugin.configManager.reload("messages.yml");
                plugin.configManager.save("messages.yml");
                plugin.reloadConfig();
                String message = ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("reload"));
                Bukkit.getConsoleSender().sendMessage(prefix + message);
            } else if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    try {
                        if (args[2] != null) {
                            int level = Integer.parseInt(args[3]);
                            if (Config.getLevelBreak(level) > 0 || level == 1) {
                                int amount = Integer.parseInt(args[2]);
                                ItemStack harvesterHoe = Extras.harvesterHoe();
                                ItemMeta itemMeta = harvesterHoe.getItemMeta();
                                List<String> updatedLore = new ArrayList<String>();
                                String sugarcaneNameColor = plugin.getMessages().getString("tool.tool_name_color");
                                String sugarcaneMainColor = plugin.getMessages().getString("tool.tool_main_color");
                                String sugarcaneSecondaryColor = plugin.getMessages().getString("tool.tool_secondary_color");
                                String sugarcaneValueColor = plugin.getMessages().getString("tool.tool_value_color");
                                for (String line : harvesterHoe.getItemMeta().getLore()) {
                                    if (line.contains("Cane Harvested:")) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Harvested: " + sugarcaneValueColor + Config.getLevelBreak(level)));
                                    } else {
                                        updatedLore.add(line);
                                    }
                                }
                                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', sugarcaneNameColor + "&lHarvester Hoe") + ChatColor.GRAY + " (Level " + level + ")");
                                updatedLore.set(4, ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Multiplier: " + sugarcaneValueColor + Config.getLevelMultiplier(level) + "x"));
                                itemMeta.setLore(updatedLore);
                                harvesterHoe.setItemMeta(itemMeta);
                                for (int i = 0; i < amount; i++) {
                                    target.getInventory().addItem(harvesterHoe);
                                }
                                System.out.println("AdvancedHarvesterHoes >> " + target.getName() + " was given " + amount + " harvester hoe(s).");
                            } else {
                                System.out.println("AdvancedHarvesterHoes >> Error: that is not a specified level in the config.");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("AdvancedHarvesterHoes >> Error: That is not a valid amount. Use: /hh give [player] [amount]");
                    }

                } else {
                    System.out.println("AdvancedHarvesterHoes >> Error: That player is not online! Use: /hh give [player] [amount]");
                }

            } else if (args.length == 2 && args[0].equalsIgnoreCase("fragment")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    ItemStack fragment = Extras.caneFragment();
                    target.getInventory().addItem(fragment);
                    System.out.println("AdvancedHarvesterHoes >> " + target + " received a cane fragment.");
                } else {
                    System.out.println("AdvancedHarvesterHoes >> Error: That player is not online! Use: /hh fragment [player]");
                }

            } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                System.out.println("AdvancedHarvesterHoes Help:");
                System.out.println("- hh give [player] [amount] [level] - Give a player the specified number of harvester hoes.");
                System.out.println("- hh fragment [player] - Give a player a cane fragment.");
                System.out.println("- hh reload - Reload the plugin.");
            } else {
                System.out.println("AdvancedHarvesterHoes Help:");
                System.out.println("- hh give [player] [amount] [level] - Give a player the specified number of harvester hoes.");
                System.out.println("- hh fragment [player] - Give a player a cane fragment.");
                System.out.println("- hh reload - Reload the plugin.");
            }
        }
        return false;
    }
}
