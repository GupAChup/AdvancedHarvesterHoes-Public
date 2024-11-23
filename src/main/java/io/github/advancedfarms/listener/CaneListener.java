package io.github.advancedfarms.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import de.tr7zw.nbtapi.NBTItem;
import io.github.advancedfarms.AdvancedFarms;
import io.github.advancedfarms.config.Config;
import io.github.advancedfarms.util.Extras;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CaneListener implements Listener {

    public HashMap<UUID, Long> inventoryFull = new HashMap<>();
    HashMap<UUID, Integer> giveMoney = new HashMap<>();
    private final AdvancedFarms plugin;
    public CaneListener(AdvancedFarms plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCaneBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        String sugarcaneNameColor;
        String sugarcaneMainColor;
        String sugarcaneSecondaryColor;
        String sugarcaneValueColor;
        if(plugin.getMessages().contains("tool")) {
            sugarcaneNameColor = plugin.getMessages().getString("tool.tool_name_color");
            sugarcaneMainColor = plugin.getMessages().getString("tool.tool_main_color");
            sugarcaneSecondaryColor = plugin.getMessages().getString("tool.tool_secondary_color");
            sugarcaneValueColor = plugin.getMessages().getString("tool.tool_value_color");
        } else {
            sugarcaneNameColor = "&a";
            sugarcaneMainColor = "&a";
            sugarcaneSecondaryColor = "&2";
            sugarcaneValueColor = "&7";
        }

        if (Extras.getItemInHand(player) != null && Extras.getItemInHand(player).getItemMeta() != null && Extras.getItemInHand(player).getItemMeta().getDisplayName() != null) {
            ItemStack item = Extras.getItemInHand(player);
            ItemMeta itemMeta = item.getItemMeta();
            List<String> lore = itemMeta.getLore();
            if (item.getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', sugarcaneNameColor + "&lHarvester Hoe"))) {
                Integer level = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getDisplayName()).replaceAll("[^0-9]", ""));
                Block block = e.getBlock();
                int amount;
                Location blockBelow = new Location(block.getWorld(), block.getX(), block.getY() - 1, block.getZ());
                if (XBlock.isSugarCane(block.getType())) {
                    if (!XBlock.isSugarCane(blockBelow.getBlock().getType())) {
                        e.setCancelled(true);

                    } else {
                        e.setCancelled(true);
                        amount = 0;
                        int upgrade = 0;
                        if (lore != null && lore.size() > 0) {
                            String jsonRadius = lore.get(1);
                            Integer radius = Integer.parseInt(ChatColor.stripColor(jsonRadius).replaceAll("[^0-9]", ""));
                            if (radius == 11) {
                                upgrade = 1;
                            } else if (radius == 33) {
                                upgrade = 2;
                            } else if (radius == 55) {
                                upgrade = 3;
                            }
                        }
                        List<Location> blocks = new ArrayList<>();
                        if (upgrade == 2) {
                            blocks.add(new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() - 1));
                            blocks.add(new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ()));
                            blocks.add(new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() + 1));
                            blocks.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() - 1));
                            blocks.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()));
                            blocks.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() + 1));
                            blocks.add(new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() - 1));
                            blocks.add(new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ()));
                            blocks.add(new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() + 1));
                        } else if (upgrade == 3) {
                            blocks.add(new Location(block.getWorld(), block.getX() + 2, block.getY(), block.getZ() - 2));
                            blocks.add(new Location(block.getWorld(), block.getX() + 2, block.getY(), block.getZ() - 1));
                            blocks.add(new Location(block.getWorld(), block.getX() + 2, block.getY(), block.getZ()));
                            blocks.add(new Location(block.getWorld(), block.getX() + 2, block.getY(), block.getZ() + 1));
                            blocks.add(new Location(block.getWorld(), block.getX() + 2, block.getY(), block.getZ() + 2));
                            blocks.add(new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() - 2));
                            blocks.add(new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() - 1));
                            blocks.add(new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ()));
                            blocks.add(new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() + 1));
                            blocks.add(new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() + 2));
                            blocks.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() - 2));
                            blocks.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() - 1));
                            blocks.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()));
                            blocks.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() + 1));
                            blocks.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() + 2));
                            blocks.add(new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() - 2));
                            blocks.add(new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() - 1));
                            blocks.add(new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ()));
                            blocks.add(new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() + 1));
                            blocks.add(new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() + 2));
                            blocks.add(new Location(block.getWorld(), block.getX() - 2, block.getY(), block.getZ() - 2));
                            blocks.add(new Location(block.getWorld(), block.getX() - 2, block.getY(), block.getZ() - 1));
                            blocks.add(new Location(block.getWorld(), block.getX() - 2, block.getY(), block.getZ()));
                            blocks.add(new Location(block.getWorld(), block.getX() - 2, block.getY(), block.getZ() + 1));
                            blocks.add(new Location(block.getWorld(), block.getX() - 2, block.getY(), block.getZ() + 2));
                        } else {
                            blocks.add(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()));
                        }
                        for (Location location : blocks) {
                            if (XBlock.isSugarCane(location.getBlock().getType())) {
                                int tempAmount = 1;
                                for (int i = location.getBlockY() + 1; i < 256; i++) {
                                    Location newblock = new Location(location.getBlock().getWorld(), location.getBlock().getX(), i, location.getBlock().getZ());
                                    if (XBlock.isSugarCane(newblock.getBlock().getType())) {
                                        tempAmount = tempAmount + 1;
                                    } else {
                                        break;
                                    }
                                }
                                for (int i = location.getBlockY() + tempAmount - 1; i > location.getBlockY() - 1; i--) {
                                    Location breakBlock = new Location(location.getWorld(), location.getBlockX(), i, location.getBlockZ());
                                    breakBlock.getBlock().setType(Material.AIR);

                                }
                                amount = amount + tempAmount;
                            }
                        }

                        Double multiplier = Config.getLevelMultiplier(level);
                        Integer roundedMultiplier = (int) Math.ceil(Config.getLevelMultiplier(level));
                        Integer subtract = roundedMultiplier - 1;
                        Random ran = new Random();
                        int choice = ran.nextInt(100) + 1;
                        if (choice <= ((multiplier - subtract) * 100) && choice > 0) {
                            amount = amount * roundedMultiplier;
                        } else {
                            if (multiplier > 1) {
                                amount = amount * subtract;
                            }
                        }

                        int fragmentAmount = 0;
                        NBTItem nbitem = new NBTItem(item);
                        Integer multiplierLevel = nbitem.getInteger("FragMultiplier");
                        Double fragmentMultiplier = 1.0;
                        if (multiplierLevel != null && multiplierLevel > 0) {
                            fragmentMultiplier = Config.getFragmentMultiplierMultiplier(multiplierLevel);
                            if (fragmentMultiplier <= 0) {
                                fragmentMultiplier = 1.0;
                            }
                        } else {
                            fragmentMultiplier = 1.0;
                        }

                        Double fragmentChance = Config.getFragmentChance() * fragmentMultiplier;
                        Random fragmentRan = new Random();
                        int fragmentChoice = fragmentRan.nextInt(1000000) + 1;
                        if (fragmentChoice <= (fragmentChance * 10000) && fragmentChoice > 0) {
                            fragmentAmount = 1;
                        }

                        boolean customStatus;
                        if (!plugin.getConfig().contains("upgrades.custom")) {
                            customStatus = false;
                        } else {
                            if (!Config.getCustomUpgradeStatus()) {
                                customStatus = false;
                            } else {
                                customStatus = true;
                            }
                        }

                        if(customStatus) {
                            int customAmount = 0;
                            Integer customLevel = nbitem.getInteger("CustomMultiplier");
                            Double customMultiplier = 1.0;
                            if (customLevel > 0) {
                                customMultiplier = Config.getCustomMultiplier(customLevel);
                            }
                            Double customChance = Config.getCustomChance() * customMultiplier;
                            Random customRan = new Random();
                            int customChoice = customRan.nextInt(1000000) + 1;
                            if (customChoice <= (customChance * 10000) && customChoice > 0) {
                                customAmount = 1;
                            }

                            if (customAmount == 1) {
                                List<String> commands = new ArrayList<>();
                                Iterator commandsIterator = plugin.getConfig().getStringList("upgrades.custom.commands").iterator();

                                while (commandsIterator.hasNext()) {
                                    String command = (String) commandsIterator.next();
                                    commands.add(command);
                                }
                                for (int i = 0; i < commands.size(); i++) {
                                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                    String command = commands.get(i);
                                    command = command.replace("{player}", player.getName());
                                    Bukkit.dispatchCommand(console, command);
                                }
                                String message = plugin.getMessages().getString("found-custom-drop");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1.0F, 1.0F);
                            }
                        }

                        ItemStack fragment = Extras.caneFragment(player);

                        if (amount > 0) {
                            if (lore != null && lore.size() > 0) {
                                Boolean autoSellStatus = nbitem.getBoolean("AutoSellStatus");
                                if (!autoSellStatus) {

                                    int inventorySpace = player.getInventory().firstEmpty();
                                    ItemStack sugarCane = new ItemStack(XMaterial.SUGAR_CANE.parseMaterial(), amount);

                                    if (inventorySpace == -1) {
                                        if (inventoryFull.containsKey(player.getUniqueId())) {
                                            if (inventoryFull.get(player.getUniqueId()) <= System.currentTimeMillis()) {
                                                Extras.send(player, ChatColor.translateAlternateColorCodes('&', "&c&lInventory Full!"), "", 5, 20, 5);
                                                player.playSound(player.getLocation(), XSound.BLOCK_CHEST_CLOSE.parseSound(), 1.0F, 1.0F);
                                                inventoryFull.put(player.getUniqueId(), System.currentTimeMillis() + 3000);
                                            }

                                        } else {
                                            Extras.send(player, ChatColor.translateAlternateColorCodes('&', "&c&lInventory Full!"), "", 5, 20, 5);
                                            player.playSound(player.getLocation(), XSound.BLOCK_CHEST_CLOSE.parseSound(), 1.0F, 1.0F);
                                            inventoryFull.put(player.getUniqueId(), System.currentTimeMillis() + 3000);
                                        }

                                        if (fragmentAmount > 0) {
                                            player.getWorld().dropItem(player.getLocation(), fragment);
                                            String message = plugin.getMessages().getString("found-drop");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                            player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1.0F, 1.0F);
                                        }
                                        player.getWorld().dropItem(player.getLocation(), sugarCane);
                                    } else {
                                        if (Config.getSoundStatus() == true) {
                                            player.playSound(player.getLocation(), XSound.ENTITY_ARROW_HIT_PLAYER.parseSound(), 15.0F, 1.0F);
                                        }
                                        if (fragmentAmount > 0) {
                                            player.getInventory().addItem(fragment);
                                            String message = plugin.getMessages().getString("found-drop");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                            player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1.0F, 1.0F);
                                        }
                                        player.getInventory().addItem(sugarCane);
                                    }
                                } else {
                                    int inventorySpace = player.getInventory().firstEmpty();
                                    if (Config.getSoundStatus() == true) {
                                        player.playSound(player.getLocation(), XSound.ENTITY_ARROW_HIT_PLAYER.parseSound(), 15.0F, 1.0F);
                                    }
                                    if (fragmentAmount > 0) {
                                        if (inventorySpace == -1) {
                                            Extras.send(player, ChatColor.translateAlternateColorCodes('&', "&c&lInventory Full!"), "", 5, 20, 5);
                                            player.playSound(player.getLocation(), XSound.BLOCK_CHEST_CLOSE.parseSound(), 1.0F, 1.0F);
                                            String message = plugin.getMessages().getString("found-drop");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                            player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1.0F, 1.0F);
                                            player.getWorld().dropItem(player.getLocation(), fragment);
                                        } else {
                                            player.getInventory().addItem(fragment);
                                            String message = plugin.getMessages().getString("found-drop");
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                            player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_PLING.parseSound(), 1.0F, 1.0F);
                                        }
                                    }
                                    int caneSellPrice = Config.getCaneSellPrice();
                                    if(!giveMoney.containsKey(player.getUniqueId())) {
                                        giveMoney.put(player.getUniqueId(), 0);
                                    }
                                    int cash = giveMoney.get(player.getUniqueId());
                                    int newCash = cash + amount;
                                    giveMoney.put(player.getUniqueId(), newCash);
                                    int caneBeforeSell = Config.getCaneBeforeSell();
                                    if(newCash >= caneBeforeSell) {
                                        AdvancedFarms.econ.depositPlayer(player, newCash * caneSellPrice);
                                        giveMoney.remove(player.getUniqueId());
                                    }
                                }
                            }
                        }
                        if (Config.containsPath(level + 1)) {
                            List<String> updatedLore = new ArrayList<>();
                            int finalAmount = 0;
                            int nextLevel = level + 1;
                            int caneAmount = 0;
                            for (String line : item.getItemMeta().getLore()) {
                                if (line.contains("Cane Harvested:")) {
                                    caneAmount = Integer.parseInt(ChatColor.stripColor(line).replaceAll("[^0-9]", ""));
                                    finalAmount = caneAmount + amount;
                                    updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Harvested: " + sugarcaneValueColor + finalAmount));
                                } else if (line.contains("|")) {
                                    finalAmount = caneAmount + amount;
                                    Double percentage = ((finalAmount - Config.getLevelBreak(level)) / ((double) Config.getLevelBreak(nextLevel) - Config.getLevelBreak(level))) * 100;
                                    if (percentage >= (double) 1 / 40 * 100 && percentage < (double) 2 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |" + sugarcaneValueColor + "||||||||||||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 2 / 40 * 100 && percentage < (double) 3 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||" + sugarcaneValueColor + "|||||||||||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 3 / 40 * 100 && percentage < (double) 4 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||" + sugarcaneValueColor + "||||||||||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 4 / 40 * 100 && percentage < (double) 5 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||" + sugarcaneValueColor + "|||||||||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 5 / 40 * 100 && percentage < (double) 6 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||" + sugarcaneValueColor + "||||||||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 6 / 40 * 100 && percentage < (double) 7 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||" + sugarcaneValueColor + "|||||||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 7 / 40 * 100 && percentage < (double) 8 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||" + sugarcaneValueColor + "||||||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 8 / 40 * 100 && percentage < (double) 9 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||" + sugarcaneValueColor + "|||||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 9 / 40 * 100 && percentage < (double) 10 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||" + sugarcaneValueColor + "||||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 10 / 40 * 100 && percentage < (double) 11 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||" + sugarcaneValueColor + "|||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 11 / 40 * 100 && percentage < (double) 12 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||" + sugarcaneValueColor + "||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 12 / 40 * 100 && percentage < (double) 13 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||" + sugarcaneValueColor + "|||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 13 / 40 * 100 && percentage < (double) 14 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||" + sugarcaneValueColor + "||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 14 / 40 * 100 && percentage < (double) 15 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||" + sugarcaneValueColor + "|||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 15 / 40 * 100 && percentage < (double) 16 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||" + sugarcaneValueColor + "||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 16 / 40 * 100 && percentage < (double) 17 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||" + sugarcaneValueColor + "|||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 17 / 40 * 100 && percentage < (double) 18 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||" + sugarcaneValueColor + "||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 18 / 40 * 100 && percentage < (double) 19 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||||" + sugarcaneValueColor + "|||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 19 / 40 * 100 && percentage < (double) 20 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||||" + sugarcaneValueColor + "||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 20 / 40 * 100 && percentage < (double) 21 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||||||" + sugarcaneValueColor + "|||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 21 / 40 * 100 && percentage < (double) 22 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||||||" + sugarcaneValueColor + "||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 22 / 40 * 100 && percentage < (double) 23 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||||||||" + sugarcaneValueColor + "|||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 23 / 40 * 100 && percentage < (double) 24 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||||||||" + sugarcaneValueColor + "||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 24 / 40 * 100 && percentage < (double) 25 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||||||||||" + sugarcaneValueColor + "|||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 25 / 40 * 100 && percentage < (double) 26 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||||||||||" + sugarcaneValueColor + "||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 26 / 40 * 100 && percentage < (double) 27 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||||||||||||" + sugarcaneValueColor + "|||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 27 / 40 * 100 && percentage < (double) 28 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||||||||||||" + sugarcaneValueColor + "||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 28 / 40 * 100 && percentage < (double) 29 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||||||||||||||" + sugarcaneValueColor + "|||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 29 / 40 * 100 && percentage < (double) 30 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||||||||||||||" + sugarcaneValueColor + "||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 30 / 40 * 100 && percentage < (double) 31 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||||||||||||||||" + sugarcaneValueColor + "|||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 31 / 40 * 100 && percentage < (double) 32 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||||||||||||||||" + sugarcaneValueColor + "||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 32 / 40 * 100 && percentage < (double) 33 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||||||||||||||||||" + sugarcaneValueColor + "|||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 33 / 40 * 100 && percentage < (double) 34 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||||||||||||||||||" + sugarcaneValueColor + "||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 34 / 40 * 100 && percentage < (double) 35 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||||||||||||||||||||" + sugarcaneValueColor + "|||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 35 / 40 * 100 && percentage < (double) 36 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||||||||||||||||||||" + sugarcaneValueColor + "||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 36 / 40 * 100 && percentage < (double) 37 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||||||||||||||||||||||" + sugarcaneValueColor + "|||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 37 / 40 * 100 && percentage < (double) 38 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||||||||||||||||||||||" + sugarcaneValueColor + "||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 38 / 40 * 100 && percentage < (double) 39 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " ||||||||||||||||||||||||||||||||||||||" + sugarcaneValueColor + "|| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else if (percentage >= (double) 39 / 40 * 100 && percentage < (double) 40 / 40 * 100) {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneMainColor + " |||||||||||||||||||||||||||||||||||||||" + sugarcaneValueColor + "| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    } else {
                                        updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level " + level + sugarcaneValueColor + " |||||||||||||||||||||||||||||||||||||||| " + sugarcaneSecondaryColor + "Level " + nextLevel));
                                    }
                                } else if (line.contains("Cane Multiplier")) {
                                    updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Multiplier: " + sugarcaneValueColor + Config.getLevelMultiplier(level) + "x"));
                                } else if (line.contains("Fragments Harvested")) {
                                    int fragmentCounter = Integer.parseInt(ChatColor.stripColor(line).replaceAll("[^0-9]", ""));
                                    if (fragmentAmount > 0) {
                                        fragmentCounter = fragmentCounter + 1;
                                    }
                                    updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Fragments Harvested: " + sugarcaneValueColor + fragmentCounter));
                                } else {
                                    updatedLore.add(line);
                                }
                            }
                            if (finalAmount > Config.getLevelBreak(nextLevel) - 1) {
                                if (Config.getLevelUpMessageStatus() == true) {
                                    Extras.send(player, ChatColor.translateAlternateColorCodes('&', "&a&lLevel Up!"), ChatColor.translateAlternateColorCodes('&', "&7&o(Level " + nextLevel + ")"), 5, 20, 5);
                                    player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 15.0F, 1.0F);
                                }
                                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', sugarcaneNameColor + "&lHarvester Hoe") + ChatColor.GRAY + " (Level " + nextLevel + ")");
                                updatedLore.set(4, ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Multiplier: " + sugarcaneValueColor + Config.getLevelMultiplier(level + 1) + "x"));
                            }
                            itemMeta.setLore(updatedLore);
                            item.setItemMeta(itemMeta);
                        } else {
                            List<String> updatedLore = new ArrayList<>();
                            int finalAmount = 0;
                            int nextLevel = level + 1;
                            int caneAmount = 0;
                            for (String line : item.getItemMeta().getLore()) {
                                if (line.contains("Cane Harvested:")) {
                                    caneAmount = Integer.parseInt(ChatColor.stripColor(line).replaceAll("[^0-9]", ""));
                                    finalAmount = caneAmount + amount;
                                    updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Harvested: " + sugarcaneValueColor + finalAmount));
                                } else if (line.contains("|")) {
                                    updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneMainColor +  "&ko" + sugarcaneSecondaryColor + " &lMAX LEVEL " + sugarcaneMainColor + "&ko"));
                                } else if (line.contains("Cane Multiplier")) {
                                    updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Multiplier: " + sugarcaneValueColor + Config.getLevelMultiplier(level) + "x"));
                                } else if (line.contains("Fragments Harvested")) {
                                    int fragmentCounter = Integer.parseInt(ChatColor.stripColor(line).replaceAll("[^0-9]", ""));
                                    if (fragmentAmount > 0) {
                                        fragmentCounter = fragmentCounter + 1;
                                    }
                                    updatedLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Fragments Harvested: " + sugarcaneValueColor + fragmentCounter));
                                } else {
                                    updatedLore.add(line);
                                }
                            }
                            itemMeta.setLore(updatedLore);
                            item.setItemMeta(itemMeta);
                        }
                    }
                }
            }
        }
    }
}