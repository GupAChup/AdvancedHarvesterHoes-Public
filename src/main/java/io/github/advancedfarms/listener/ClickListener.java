package io.github.advancedfarms.listener;

import java.util.List;

import com.cryptomorin.xseries.XSound;
import de.tr7zw.nbtapi.NBTItem;
import io.github.advancedfarms.AdvancedFarms;
import io.github.advancedfarms.config.Config;
import io.github.advancedfarms.util.Extras;
import io.github.advancedfarms.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClickListener implements Listener {

    private final AdvancedFarms plugin;

    public ClickListener(AdvancedFarms plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        String guiName;
        if(plugin.getMessages().contains("gui.gui_name")) {
            guiName = plugin.getMessages().getString("gui.gui_name");
        } else {
            guiName = ChatColor.translateAlternateColorCodes('&', "&a&lUpgrades Menu");
        }

        String sugarcaneMainColor;
        String sugarcaneSecondaryColor;
        String sugarcaneValueColor;
        if(plugin.getMessages().contains("tool")) {
            sugarcaneMainColor = plugin.getMessages().getString("tool.tool_main_color");
            sugarcaneSecondaryColor = plugin.getMessages().getString("tool.tool_secondary_color");
            sugarcaneValueColor = plugin.getMessages().getString("tool.tool_value_color");
        } else {
            sugarcaneMainColor = "&a";
            sugarcaneSecondaryColor = "&2";
            sugarcaneValueColor = "&7";
        }

        if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', guiName))) {
            Player player = (Player) e.getWhoClicked();
            ItemStack item = Extras.getItemInHand(player);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            e.setCancelled(true);
            int fragmentsInInventory = 0;
            int removedFragments = 0;
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) != null) {
                    ItemStack itemInven = player.getInventory().getItem(i);
                    if (itemInven.getItemMeta() != null && itemInven.getItemMeta().getDisplayName() != null) {
                        ItemMeta metaInven = itemInven.getItemMeta();
                        if (metaInven.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                            fragmentsInInventory = fragmentsInInventory + 1;
                        }
                    }
                }
            }
            if (e.getRawSlot() == 20) {
                NBTItem nitem = new NBTItem(item);
                Boolean autoSellStatus = nitem.getBoolean("AutoSellStatus");                if (!autoSellStatus) {
                    int priceMoney = Config.getAutoSellCostMoney();
                    int priceFrags = Config.getAutoSellCostFragments();
                    if (AdvancedFarms.econ.getBalance(player) >= priceMoney && fragmentsInInventory >= priceFrags) {
                        AdvancedFarms.econ.withdrawPlayer(player, priceMoney);
                        for (int i = 0; i < player.getInventory().getSize(); i++) {
                            if (player.getInventory().getItem(i) != null) {
                                ItemStack itemInv = player.getInventory().getItem(i);
                                if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                    ItemMeta metaInv = itemInv.getItemMeta();
                                    if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                        player.getInventory().remove(itemInv);
                                        removedFragments = removedFragments + 1;
                                    }
                                }
                                if (removedFragments == priceFrags) {
                                    break;
                                }
                            }
                        }
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        int index = 0;
                        for (ItemStack items : player.getInventory().getContents()) {
                            if (item.isSimilar(items)) {
                                NBTItem nbitem = new NBTItem(item);
                                nbitem.setBoolean("AutoSellStatus", true);
                                player.getInventory().setItem(index, nbitem.getItem());
                                player.updateInventory();
                            }
                            index++;
                        }
                        player.closeInventory();
                        String message = plugin.getMessages().getString("purchase");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                    } else if (AdvancedFarms.econ.getBalance(player) < priceMoney && fragmentsInInventory >= priceFrags) {
                        String message = plugin.getMessages().getString("invalid-money");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                    } else if (AdvancedFarms.econ.getBalance(player) >= priceMoney && fragmentsInInventory < priceFrags) {
                        String message = plugin.getMessages().getString("invalid-fragments");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                    } else {
                        String message = plugin.getMessages().getString("invalid-money-and-fragments");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                    }
                } else {
                    String message = plugin.getMessages().getString("purchased-max-upgrade");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                }
            } else if (e.getRawSlot() == 22) {
                List<String> radiusLore = lore;
                if (lore != null && lore.size() > 0) {
                    String json = lore.get(1);
                    Integer amount = Integer.parseInt(ChatColor.stripColor(json).replaceAll("[^0-9]", ""));
                    int upgrade = 0;
                    if (amount == 11) {
                        upgrade = 1;
                    } else if (amount == 33) {
                        upgrade = 2;
                    } else if (amount == 55) {
                        upgrade = 3;
                    }
                    Integer priceMoney = Config.getRadiusCostMoney(upgrade);
                    Integer priceFrags = Config.getRadiusCostFragments(upgrade);
                    if (upgrade == 1) {
                        if (AdvancedFarms.econ.getBalance(player) >= priceMoney && fragmentsInInventory >= priceFrags) {
                            AdvancedFarms.econ.withdrawPlayer(player, priceMoney);
                            for (int i = 0; i < player.getInventory().getSize(); i++) {
                                if (player.getInventory().getItem(i) != null) {
                                    ItemStack itemInv = player.getInventory().getItem(i);
                                    if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                        ItemMeta metaInv = itemInv.getItemMeta();
                                        if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                            player.getInventory().remove(itemInv);
                                            removedFragments = removedFragments + 1;
                                        }
                                    }
                                    if (removedFragments == priceFrags) {
                                        break;
                                    }
                                }
                            }
                            lore.set(1, ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Radius: " + sugarcaneValueColor + "(3x3)"));
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            player.closeInventory();
                            String message = plugin.getMessages().getString("purchase");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) < priceMoney && fragmentsInInventory >= priceFrags) {
                            String message = plugin.getMessages().getString("invalid-money");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) >= priceMoney && fragmentsInInventory < priceFrags) {
                            String message = plugin.getMessages().getString("invalid-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else {
                            String message = plugin.getMessages().getString("invalid-money-and-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        }
                    } else if (upgrade == 2) {
                        if (AdvancedFarms.econ.getBalance(player) >= priceMoney && fragmentsInInventory >= priceFrags) {
                            AdvancedFarms.econ.withdrawPlayer(player, priceMoney);
                            for (int i = 0; i < player.getInventory().getSize(); i++) {
                                if (player.getInventory().getItem(i) != null) {
                                    ItemStack itemInv = player.getInventory().getItem(i);
                                    if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                        ItemMeta metaInv = itemInv.getItemMeta();
                                        if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                            player.getInventory().remove(itemInv);
                                            removedFragments = removedFragments + 1;
                                        }
                                    }
                                    if (removedFragments == priceFrags) {
                                        break;
                                    }
                                }
                            }
                            lore.set(1, ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Radius: " + sugarcaneValueColor + "(5x5)"));
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            player.closeInventory();
                            String message = plugin.getMessages().getString("purchase");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) < priceMoney && fragmentsInInventory >= priceFrags) {
                            String message = plugin.getMessages().getString("invalid-money");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) >= priceMoney && fragmentsInInventory < priceFrags) {
                            String message = plugin.getMessages().getString("invalid-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else {
                            String message = plugin.getMessages().getString("invalid-money-and-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        }
                    } else if (upgrade == 3) {
                        String message = plugin.getMessages().getString("purchased-max-upgrade");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                    }
                }
            } else if (e.getRawSlot() == 24) {
                if (lore != null && lore.size() > 0) {
                    NBTItem nbitem = new NBTItem(item);
                    Integer amount = nbitem.getInteger("FragMultiplier");                    Integer costMoney = Config.getFragmentMultiplierCostMoney(amount + 1);
                    Integer costFrags = Config.getFragmentMultiplierCostFragments(amount + 1);
                    if (amount == 0) {
                        if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory >= costFrags) {
                            AdvancedFarms.econ.withdrawPlayer(player, costMoney);
                            for (int i = 0; i < player.getInventory().getSize(); i++) {
                                if (player.getInventory().getItem(i) != null) {
                                    ItemStack itemInv = player.getInventory().getItem(i);
                                    if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                        ItemMeta metaInv = itemInv.getItemMeta();
                                        if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                            player.getInventory().remove(itemInv);
                                            removedFragments = removedFragments + 1;
                                        }
                                    }
                                    if (removedFragments == costFrags) {
                                        break;
                                    }
                                }
                            }
                            for (int index = 0; index < player.getInventory().getSize(); index++) {
                                ItemStack items = player.getInventory().getItem(index);
                                if (items != null && items.isSimilar(item)) {
                                    nbitem.setInteger("FragMultiplier", 1);
                                    player.getInventory().setItem(index, nbitem.getItem());
                                }
                            }
                            player.updateInventory();
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            player.closeInventory();
                            String message = plugin.getMessages().getString("purchase");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) < costMoney && fragmentsInInventory >= costFrags) {
                            String message = plugin.getMessages().getString("invalid-money");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory < costFrags) {
                            String message = plugin.getMessages().getString("invalid-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else {
                            String message = plugin.getMessages().getString("invalid-money-and-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        }
                    } else if (amount == 1) {
                        if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory >= costFrags) {
                            AdvancedFarms.econ.withdrawPlayer(player, costMoney);
                            for (int i = 0; i < player.getInventory().getSize(); i++) {
                                if (player.getInventory().getItem(i) != null) {
                                    ItemStack itemInv = player.getInventory().getItem(i);
                                    if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                        ItemMeta metaInv = itemInv.getItemMeta();
                                        if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                            player.getInventory().remove(itemInv);
                                            removedFragments = removedFragments + 1;
                                        }
                                    }
                                    if (removedFragments == costFrags) {
                                        break;
                                    }
                                }
                            }
                            int index = 0;
                            for (ItemStack items : player.getInventory().getContents()) {
                                if (items != null && item.isSimilar(items)) {
                                    if (!nbitem.hasKey("FragMultiplier")) {
                                        nbitem.setInteger("FragMultiplier", 0);
                                    }
                                    nbitem.setInteger("FragMultiplier", 2);
                                    player.getInventory().setItem(index, nbitem.getItem());
                                }
                                index++;
                            }
                            player.updateInventory();
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            player.closeInventory();
                            String message = plugin.getMessages().getString("purchase");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) < costMoney && fragmentsInInventory >= costFrags) {
                            String message = plugin.getMessages().getString("invalid-money");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory < costFrags) {
                            String message = plugin.getMessages().getString("invalid-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else {
                            String message = plugin.getMessages().getString("invalid-money-and-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        }
                    } else if (amount == 2) {
                        if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory >= costFrags) {
                            AdvancedFarms.econ.withdrawPlayer(player, costMoney);
                            for (int i = 0; i < player.getInventory().getSize(); i++) {
                                if (player.getInventory().getItem(i) != null) {
                                    ItemStack itemInv = player.getInventory().getItem(i);
                                    if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                        ItemMeta metaInv = itemInv.getItemMeta();
                                        if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                            player.getInventory().remove(itemInv);
                                            removedFragments = removedFragments + 1;
                                        }
                                    }
                                    if (removedFragments == costFrags) {
                                        break;
                                    }
                                }
                            }
                            int index = 0;
                            for (ItemStack items : player.getInventory().getContents()) {
                                if (items != null && item.isSimilar(items)) {
                                    if (!nbitem.hasKey("FragMultiplier")) {
                                        nbitem.setInteger("FragMultiplier", 0);
                                    }
                                    nbitem.setInteger("FragMultiplier", 3);
                                    player.getInventory().setItem(index, nbitem.getItem());
                                }
                                index++;
                            }
                            player.updateInventory();
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            player.closeInventory();
                            String message = plugin.getMessages().getString("purchase");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) < costMoney && fragmentsInInventory >= costFrags) {
                            String message = plugin.getMessages().getString("invalid-money");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory < costFrags) {
                            String message = plugin.getMessages().getString("invalid-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else {
                            String message = plugin.getMessages().getString("invalid-money-and-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        }
                    } else if (amount == 3) {
                        if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory >= costFrags) {
                            AdvancedFarms.econ.withdrawPlayer(player, costMoney);
                            for (int i = 0; i < player.getInventory().getSize(); i++) {
                                if (player.getInventory().getItem(i) != null) {
                                    ItemStack itemInv = player.getInventory().getItem(i);
                                    if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                        ItemMeta metaInv = itemInv.getItemMeta();
                                        if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                            player.getInventory().remove(itemInv);
                                            removedFragments = removedFragments + 1;
                                        }
                                    }
                                    if (removedFragments == costFrags) {
                                        break;
                                    }
                                }
                            }
                            int index = 0;
                            for (ItemStack items : player.getInventory().getContents()) {
                                if (items != null && item.isSimilar(items)) {
                                    if (!nbitem.hasKey("FragMultiplier")) {
                                        nbitem.setInteger("FragMultiplier", 0);
                                    }
                                    nbitem.setInteger("FragMultiplier", 4);
                                    player.getInventory().setItem(index, nbitem.getItem());
                                }
                                index++;
                            }
                            player.updateInventory();

                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            player.closeInventory();
                            String message = plugin.getMessages().getString("purchase");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) < costMoney && fragmentsInInventory >= costFrags) {
                            String message = plugin.getMessages().getString("invalid-money");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory < costFrags) {
                            String message = plugin.getMessages().getString("invalid-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else {
                            String message = plugin.getMessages().getString("invalid-money-and-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        }
                    } else if (amount == 4) {
                        if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory >= costFrags) {
                            AdvancedFarms.econ.withdrawPlayer(player, costMoney);
                            for (int i = 0; i < player.getInventory().getSize(); i++) {
                                if (player.getInventory().getItem(i) != null) {
                                    ItemStack itemInv = player.getInventory().getItem(i);
                                    if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                        ItemMeta metaInv = itemInv.getItemMeta();
                                        if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                            player.getInventory().remove(itemInv);
                                            removedFragments = removedFragments + 1;
                                        }
                                    }
                                    if (removedFragments == costFrags) {
                                        break;
                                    }
                                }
                            }
                            int index = 0;
                            for (ItemStack items : player.getInventory().getContents()) {
                                if (items != null && item.isSimilar(items)) {
                                    if (!nbitem.hasKey("FragMultiplier")) {
                                        nbitem.setInteger("FragMultiplier", 0);
                                    }
                                    nbitem.setInteger("FragMultiplier", 5);
                                    player.getInventory().setItem(index, nbitem.getItem());
                                }
                                index++;
                            }
                            player.updateInventory();
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            player.closeInventory();
                            String message = plugin.getMessages().getString("purchase");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) < costMoney && fragmentsInInventory >= costFrags) {
                            String message = plugin.getMessages().getString("invalid-money");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory < costFrags) {
                            String message = plugin.getMessages().getString("invalid-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        } else {
                            String message = plugin.getMessages().getString("invalid-money-and-fragments");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                        }
                    } else if (amount == 5) {
                        String message = plugin.getMessages().getString("purchased-max-upgrade");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                    }
                }
            } else if (e.getRawSlot() == 40) {
                if(plugin.getConfig().contains("upgrades.custom")) {
                    if(Config.getCustomUpgradeStatus()) {
                        if (lore != null && lore.size() > 0) {
                            NBTItem nbitem = new NBTItem(item);
                            Integer amount = nbitem.getInteger("CustomMultiplier");
                            Integer costMoney = Config.getCustomCostMoney(amount + 1);
                            Integer costFrags = Config.getCustomCostFragments(amount + 1);
                            if (amount == 0) {
                                if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory >= costFrags) {
                                    AdvancedFarms.econ.withdrawPlayer(player, costMoney);
                                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                                        if (player.getInventory().getItem(i) != null) {
                                            ItemStack itemInv = player.getInventory().getItem(i);
                                            if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                                ItemMeta metaInv = itemInv.getItemMeta();
                                                if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                                    player.getInventory().remove(itemInv);
                                                    removedFragments = removedFragments + 1;
                                                }
                                            }
                                            if (removedFragments == costFrags) {
                                                break;
                                            }
                                        }
                                    }
                                    int index = 0;
                                    for (ItemStack items : player.getInventory().getContents()) {
                                        if (item.isSimilar(items)) {
                                            nbitem.setInteger("CustomMultiplier", 1);
                                            player.getInventory().setItem(index, nbitem.getItem());
                                        }
                                        index++;
                                    }
                                    player.updateInventory();
                                    meta.setLore(lore);
                                    item.setItemMeta(meta);
                                    player.closeInventory();
                                    String message = plugin.getMessages().getString("purchase");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                                } else if (AdvancedFarms.econ.getBalance(player) < costMoney && fragmentsInInventory >= costFrags) {
                                    String message = plugin.getMessages().getString("invalid-money");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                } else if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory < costFrags) {
                                    String message = plugin.getMessages().getString("invalid-fragments");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                } else {
                                    String message = plugin.getMessages().getString("invalid-money-and-fragments");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                }
                            } else if (amount == 1) {
                                if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory >= costFrags) {
                                    AdvancedFarms.econ.withdrawPlayer(player, costMoney);
                                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                                        if (player.getInventory().getItem(i) != null) {
                                            ItemStack itemInv = player.getInventory().getItem(i);
                                            if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                                ItemMeta metaInv = itemInv.getItemMeta();
                                                if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                                    player.getInventory().remove(itemInv);
                                                    removedFragments = removedFragments + 1;
                                                }
                                            }
                                            if (removedFragments == costFrags) {
                                                break;
                                            }
                                        }
                                    }
                                    int index = 0;
                                    for (ItemStack items : player.getInventory().getContents()) {
                                        if (item.isSimilar(items)) {
                                            nbitem.setInteger("CustomMultiplier", 2);
                                            item = nbitem.getItem();
                                            player.getInventory().setItem(index, item);
                                            player.updateInventory();
                                        }
                                        index++;
                                    }
                                    meta.setLore(lore);
                                    item.setItemMeta(meta);
                                    player.closeInventory();
                                    String message = plugin.getMessages().getString("purchase");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                                } else if (AdvancedFarms.econ.getBalance(player) < costMoney && fragmentsInInventory >= costFrags) {
                                    String message = plugin.getMessages().getString("invalid-money");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                } else if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory < costFrags) {
                                    String message = plugin.getMessages().getString("invalid-fragments");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                } else {
                                    String message = plugin.getMessages().getString("invalid-money-and-fragments");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                }
                            } else if (amount == 2) {
                                if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory >= costFrags) {
                                    AdvancedFarms.econ.withdrawPlayer(player, costMoney);
                                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                                        if (player.getInventory().getItem(i) != null) {
                                            ItemStack itemInv = player.getInventory().getItem(i);
                                            if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                                ItemMeta metaInv = itemInv.getItemMeta();
                                                if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                                    player.getInventory().remove(itemInv);
                                                    removedFragments = removedFragments + 1;
                                                }
                                            }
                                            if (removedFragments == costFrags) {
                                                break;
                                            }
                                        }
                                    }
                                    int index = 0;
                                    for (ItemStack items : player.getInventory().getContents()) {
                                        if (item.isSimilar(items)) {
                                            nbitem.setInteger("CustomMultiplier", 3);
                                            item = nbitem.getItem();
                                            player.getInventory().setItem(index, item);
                                            player.updateInventory();
                                        }
                                        index++;
                                    }
                                    meta.setLore(lore);
                                    item.setItemMeta(meta);
                                    player.closeInventory();
                                    String message = plugin.getMessages().getString("purchase");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                                } else if (AdvancedFarms.econ.getBalance(player) < costMoney && fragmentsInInventory >= costFrags) {
                                    String message = plugin.getMessages().getString("invalid-money");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                } else if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory < costFrags) {
                                    String message = plugin.getMessages().getString("invalid-fragments");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                } else {
                                    String message = plugin.getMessages().getString("invalid-money-and-fragments");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                }
                            } else if (amount == 3) {
                                if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory >= costFrags) {
                                    AdvancedFarms.econ.withdrawPlayer(player, costMoney);
                                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                                        if (player.getInventory().getItem(i) != null) {
                                            ItemStack itemInv = player.getInventory().getItem(i);
                                            if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                                ItemMeta metaInv = itemInv.getItemMeta();
                                                if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                                    player.getInventory().remove(itemInv);
                                                    removedFragments = removedFragments + 1;
                                                }
                                            }
                                            if (removedFragments == costFrags) {
                                                break;
                                            }
                                        }
                                    }
                                    int index = 0;
                                    for (ItemStack items : player.getInventory().getContents()) {
                                        if (item.isSimilar(items)) {
                                            nbitem.setInteger("CustomMultiplier", 4);
                                            item = nbitem.getItem();
                                            player.getInventory().setItem(index, item);
                                            player.updateInventory();
                                        }
                                        index++;
                                    }
                                    meta.setLore(lore);
                                    item.setItemMeta(meta);
                                    player.closeInventory();
                                    String message = plugin.getMessages().getString("purchase");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                                } else if (AdvancedFarms.econ.getBalance(player) < costMoney && fragmentsInInventory >= costFrags) {
                                    String message = plugin.getMessages().getString("invalid-money");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                } else if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory < costFrags) {
                                    String message = plugin.getMessages().getString("invalid-fragments");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                } else {
                                    String message = plugin.getMessages().getString("invalid-money-and-fragments");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                }
                            } else if (amount == 4) {
                                if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory >= costFrags) {
                                    AdvancedFarms.econ.withdrawPlayer(player, costMoney);
                                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                                        if (player.getInventory().getItem(i) != null) {
                                            ItemStack itemInv = player.getInventory().getItem(i);
                                            if (itemInv.getItemMeta() != null && itemInv.getItemMeta().getDisplayName() != null) {
                                                ItemMeta metaInv = itemInv.getItemMeta();
                                                if (metaInv.getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")))) {
                                                    player.getInventory().remove(itemInv);
                                                    removedFragments = removedFragments + 1;
                                                }
                                            }
                                            if (removedFragments == costFrags) {
                                                break;
                                            }
                                        }
                                    }
                                    int index = 0;
                                    for (ItemStack items : player.getInventory().getContents()) {
                                        if (item.isSimilar(items)) {
                                            nbitem.setInteger("CustomMultiplier", 5);
                                            item = nbitem.getItem();
                                            player.getInventory().setItem(index, item);
                                            player.updateInventory();
                                        }
                                        index++;
                                    }
                                    meta.setLore(lore);
                                    item.setItemMeta(meta);
                                    player.closeInventory();
                                    String message = plugin.getMessages().getString("purchase");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
                                } else if (AdvancedFarms.econ.getBalance(player) < costMoney && fragmentsInInventory >= costFrags) {
                                    String message = plugin.getMessages().getString("invalid-money");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                } else if (AdvancedFarms.econ.getBalance(player) >= costMoney && fragmentsInInventory < costFrags) {
                                    String message = plugin.getMessages().getString("invalid-fragments");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                } else {
                                    String message = plugin.getMessages().getString("invalid-money-and-fragments");
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                                }
                            } else if (amount == 5) {
                                String message = plugin.getMessages().getString("purchased-max-upgrade");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                            }
                        }
                    }
                }
            }
        }
    }
}