package com.chup.advancedharvesterhoes;

import com.chup.advancedharvesterhoes.extras.NBTEditor;
import com.chup.advancedharvesterhoes.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class UpgradeGUI {

    private Main plugin;

    public UpgradeGUI(Main plugin) {
        this.plugin = plugin;
    }

    public Inventory openInventory(Player player, ItemStack item) {

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###,###,###");

        String guiName;
        if (plugin.getMessages().contains("gui.gui_name")) {
            guiName = plugin.getMessages().getString("gui.gui_name");
        } else {
            guiName = ChatColor.translateAlternateColorCodes('&', "&a&lUpgrades Menu");
        }

        String statusColor;
        if (plugin.getMessages().contains("gui.colors.status_color")) {
            statusColor = plugin.getMessages().getString("gui.colors.status_color");
        } else {
            statusColor = "&6";
        }
        String costColor;
        if (plugin.getMessages().contains("gui.colors.cost_color")) {
            costColor = plugin.getMessages().getString("gui.colors.cost_color");
        } else {
            costColor = "&6";
        }
        String upgradeColor;
        if (plugin.getMessages().contains("gui.colors.upgrade_color")) {
            upgradeColor = plugin.getMessages().getString("gui.colors.upgrade_color");
        } else {
            upgradeColor = "&b";
        }
        String moneyCostColor;
        if (plugin.getMessages().contains("gui.colors.money_cost_color")) {
            moneyCostColor = plugin.getMessages().getString("gui.colors.money_cost_color");
        } else {
            moneyCostColor = "&b";
        }
        String fragmentCostColor;
        if (plugin.getMessages().contains("gui.colors.fragment_cost_color")) {
            fragmentCostColor = plugin.getMessages().getString("gui.colors.fragment_cost_color");
        } else {
            fragmentCostColor = "&b";
        }
        String moneyNumberColor;
        if (plugin.getMessages().contains("gui.colors.money_number_color")) {
            moneyNumberColor = plugin.getMessages().getString("gui.colors.money_number_color");
        } else {
            moneyNumberColor = "&e";
        }
        String fragmentNumberColor;
        if (plugin.getMessages().contains("gui.colors.fragment_number_color")) {
            fragmentNumberColor = plugin.getMessages().getString("gui.colors.fragment_number_color");
        } else {
            fragmentNumberColor = "&e";
        }

        int size;
        if (!plugin.getConfig().contains("upgrades.custom")) {
            size = 45;
        } else {
            if (!Config.getCustomUpgradeStatus()) {
                size = 45;
            } else {
                size = 54;
            }
        }

        Inventory gui = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', guiName));

        ItemStack description = new ItemStack(Material.BOOK);
        ItemMeta descriptionMeta = description.getItemMeta();
        String title = plugin.getMessages().getString("gui.upgrades.name");
        descriptionMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
        List<String> descriptionLore = new ArrayList<String>();
        Iterator descriptionIterator = plugin.getMessages().getStringList("gui.upgrades.lore").iterator();
        while (descriptionIterator.hasNext()) {
            String descriptionMessage = (String) descriptionIterator.next();
            descriptionLore.add(ChatColor.translateAlternateColorCodes('&', descriptionMessage));
        }
        descriptionMeta.setLore(descriptionLore);
        description.setItemMeta(descriptionMeta);

        String autosellTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTM2ZTk0ZjZjMzRhMzU0NjVmY2U0YTkwZjJlMjU5NzYzODllYjk3MDlhMTIyNzM1NzRmZjcwZmQ0ZGFhNjg1MiJ9fX0=";
        ItemStack autoSell = Extras.getCustomSkull(autosellTexture);
        SkullMeta autoSellMeta = (SkullMeta) autoSell.getItemMeta();
        String autoSellName;
        if (plugin.getMessages().contains("gui.autosell.name")) {
            autoSellName = plugin.getMessages().getString("gui.autosell.name");
        } else {
            autoSellName = "&e&lAutoSell";
        }
        autoSellMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', autoSellName));
        List<String> autoSellLore = new ArrayList<String>();

        Iterator autoSellIterator = plugin.getMessages().getStringList("gui.autosell.lore").iterator();
        while (autoSellIterator.hasNext()) {
            String autoSellMessage = (String) autoSellIterator.next();
            autoSellLore.add(ChatColor.translateAlternateColorCodes('&', autoSellMessage));
        }

        autoSellLore.add("");
        if (lore != null && lore.size() > 0) {
            Boolean autoSellStatus = NBTEditor.getBoolean(item, "AutoSellStatus");
            Integer costMoney = Config.getAutoSellCostMoney();
            Integer costFrags = Config.getAutoSellCostFragments();
            if (!autoSellStatus) {
                autoSellLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7: &c&lLocked ✘"));
                autoSellLore.add("");
                autoSellLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                autoSellLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                autoSellLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else {
                autoSellLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7: &a&lPurchased ✔"));
            }
            autoSellMeta.setLore(autoSellLore);
        }
        autoSell.setItemMeta(autoSellMeta);

        String radiusTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNjYTlkOGIxZDVmMmMyNjU0NzY0NDAzNGE1NWE0YTI0NjNlMTY2ZjYwZmViMGM3YzVhZjNmNzI0YmFlIn19fQ==";
        ItemStack radius = Extras.getCustomSkull(radiusTexture);
        SkullMeta radiusMeta = (SkullMeta) radius.getItemMeta();
        String radiusName;
        if (plugin.getMessages().contains("gui.radius.name")) {
            radiusName = plugin.getMessages().getString("gui.radius.name");
        } else {
            radiusName = "&c&lRadius";
        }
        radiusMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', radiusName));
        List<String> radiusLore = new ArrayList<String>();

        Iterator radiusIterator = plugin.getMessages().getStringList("gui.radius.lore").iterator();
        while (radiusIterator.hasNext()) {
            String radiusMessage = (String) radiusIterator.next();
            radiusLore.add(ChatColor.translateAlternateColorCodes('&', radiusMessage));
        }

        radiusLore.add("");
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
            Integer costMoney = Config.getRadiusCostMoney(upgrade);
            Integer costFrags = Config.getRadiusCostFragments(upgrade);
            if (upgrade == 1) {
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(3x3)&7: &c&lLocked ✘"));
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(5x5)&7: &c&lLocked ✘"));
                radiusLore.add("");
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (upgrade == 2) {
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(3x3)&7: &a&lPurchased ✔"));
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(5x5)&7: &c&lLocked ✘"));
                radiusLore.add("");
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (upgrade == 3) {
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(3x3)&7: &a&lPurchased ✔"));
                radiusLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(5x5)&7: &a&lPurchased ✔"));
            } else {
                radiusLore.add(ChatColor.GOLD + "Status" + ChatColor.GRAY + ": " + ChatColor.GREEN.toString() + ChatColor.BOLD + "Purchased ✔");
            }
            radiusMeta.setLore(radiusLore);
        }
        radius.setItemMeta(radiusMeta);

        String fragmentTexture = plugin.getConfig().getString("fragment-texture");
        ItemStack fragment = Extras.getCustomSkull(fragmentTexture);
        SkullMeta fragmentMeta = (SkullMeta) fragment.getItemMeta();
        String fragmentMultiplierName;
        if (plugin.getMessages().contains("gui.fragmentmultiplier.name")) {
            fragmentMultiplierName = plugin.getMessages().getString("gui.fragmentmultiplier.name");
        } else {
            fragmentMultiplierName = "&d&lFragment Multiplier";
        }
        fragmentMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', fragmentMultiplierName));
        List<String> fragmentLore = new ArrayList<String>();

        Iterator fragmentIterator = plugin.getMessages().getStringList("gui.fragmentmultiplier.lore").iterator();
        while (fragmentIterator.hasNext()) {
            String fragmentMessage = (String) fragmentIterator.next();
            fragmentLore.add(ChatColor.translateAlternateColorCodes('&', fragmentMessage));
        }

        fragmentLore.add("");
        if (lore != null && lore.size() > 0) {
            Integer amount = NBTEditor.getInt(item, "FragMultiplier");
            Integer costMoney = Config.getFragmentMultiplierCostMoney(amount + 1);
            Integer costFrags = Config.getFragmentMultiplierCostFragments(amount + 1);
            if (amount == 0) {
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(1) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(2) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(3) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(4) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(5) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add("");
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (amount == 1) {
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(1) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(2) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(3) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(4) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(5) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add("");
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (amount == 2) {
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(1) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(2) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(3) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(4) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(5) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add("");
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (amount == 3) {
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(1) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(2) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(3) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(4) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(5) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add("");
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (amount == 4) {
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(1) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(2) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(3) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(4) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(5) + "x Boost)&7: &c&lLocked ✘"));
                fragmentLore.add("");
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (amount == 5) {
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(1) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(2) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(3) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(4) + "x Boost)&7: &a&lPurchased ✔"));
                fragmentLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getFragmentMultiplierMultiplier(5) + "x Boost)&7: &a&lPurchased ✔"));
            }
            fragmentMeta.setLore(fragmentLore);
        }
        fragment.setItemMeta(fragmentMeta);

        String customTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYyMmI2YTNhMGYyNGJkZWVhYjJhNmFjZDliMWY1MmJiOTU5NGQ1ZjZiMWUyYzA1ZGRkYjIxOTQxMGM4In19fQ==";
        ItemStack custom = Extras.getCustomSkull(customTexture);
        SkullMeta customMeta = (SkullMeta) custom.getItemMeta();
        String customName = plugin.getMessages().getString("gui.custom.name");
        customMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', customName));
        List<String> customLore = new ArrayList<String>();

        Iterator customIterator = plugin.getMessages().getStringList("gui.custom.lore").iterator();
        while (customIterator.hasNext()) {
            String customMessage = (String) customIterator.next();
            customLore.add(ChatColor.translateAlternateColorCodes('&', customMessage));
        }

        customLore.add("");
        if (lore != null && lore.size() > 0) {
            Integer amount = NBTEditor.getInt(item, "CustomMultiplier");
            Integer costMoney = Config.getCustomCostMoney(amount + 1);
            Integer costFrags = Config.getCustomCostFragments(amount + 1);
            if (amount == 0) {
                customLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(1) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(2) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(3) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(4) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(5) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add("");
                customLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                customLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (amount == 1) {
                customLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(1) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(2) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(3) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(4) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(5) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add("");
                customLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                customLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (amount == 2) {
                customLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(1) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(2) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(3) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(4) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(5) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add("");
                customLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                customLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (amount == 3) {
                customLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(1) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(2) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(3) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(4) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(5) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add("");
                customLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                customLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (amount == 4) {
                customLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(1) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(2) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(3) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(4) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(5) + "x Boost)&7: &c&lLocked ✘"));
                customLore.add("");
                customLore.add(ChatColor.translateAlternateColorCodes('&', costColor + "Cost&7:"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', moneyCostColor + " Money&7: " + moneyNumberColor + formatter.format(costMoney)));
                customLore.add(ChatColor.translateAlternateColorCodes('&', fragmentCostColor + " Fragments&7: " + fragmentNumberColor + formatter.format(costFrags)));
            } else if (amount == 5) {
                customLore.add(ChatColor.translateAlternateColorCodes('&', statusColor + "Status&7:"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 1 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(1) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 2 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(2) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 3 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(3) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 4 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(4) + "x Boost)&7: &a&lPurchased ✔"));
                customLore.add(ChatColor.translateAlternateColorCodes('&', " " + upgradeColor + "&lUpgrade 5 &7- " + upgradeColor + "&o(" + Config.getCustomMultiplier(5) + "x Boost)&7: &a&lPurchased ✔"));
            }
            customMeta.setLore(customLore);
        }
        custom.setItemMeta(customMeta);

        ItemStack mainColor = Extras.getColor(Config.getMainColor());
        ItemStack secondaryColor = Extras.getColor(Config.getSecondaryColor());

        ItemStack mainBackground;
        ItemStack secondaryBackground;

        if (plugin.getConfig().contains("gui-main-color")) {
            mainBackground = mainColor;
        } else {
            mainBackground = new ItemStack(XMaterial.GREEN_STAINED_GLASS_PANE.parseMaterial());
        }

        if (plugin.getConfig().contains("gui-secondary-color")) {
            secondaryBackground = secondaryColor;
        } else {
            secondaryBackground = new ItemStack(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial());
        }

        if (size == 45) {
            gui.setItem(4, description);
            gui.setItem(20, autoSell);
            gui.setItem(22, radius);
            gui.setItem(24, fragment);
            gui.setItem(0, mainBackground);
            gui.setItem(1, mainBackground);
            gui.setItem(2, mainBackground);
            gui.setItem(3, mainBackground);
            gui.setItem(5, mainBackground);
            gui.setItem(6, mainBackground);
            gui.setItem(7, mainBackground);
            gui.setItem(8, mainBackground);
            gui.setItem(9, mainBackground);
            gui.setItem(17, mainBackground);
            gui.setItem(18, mainBackground);
            gui.setItem(26, mainBackground);
            gui.setItem(27, mainBackground);
            gui.setItem(35, mainBackground);
            gui.setItem(36, mainBackground);
            gui.setItem(37, mainBackground);
            gui.setItem(38, mainBackground);
            gui.setItem(39, mainBackground);
            gui.setItem(40, mainBackground);
            gui.setItem(41, mainBackground);
            gui.setItem(42, mainBackground);
            gui.setItem(43, mainBackground);
            gui.setItem(44, mainBackground);
            gui.setItem(10, secondaryBackground);
            gui.setItem(11, secondaryBackground);
            gui.setItem(12, secondaryBackground);
            gui.setItem(13, secondaryBackground);
            gui.setItem(14, secondaryBackground);
            gui.setItem(15, secondaryBackground);
            gui.setItem(16, secondaryBackground);
            gui.setItem(19, secondaryBackground);
            gui.setItem(21, secondaryBackground);
            gui.setItem(23, secondaryBackground);
            gui.setItem(25, secondaryBackground);
            gui.setItem(28, secondaryBackground);
            gui.setItem(29, secondaryBackground);
            gui.setItem(30, secondaryBackground);
            gui.setItem(31, secondaryBackground);
            gui.setItem(32, secondaryBackground);
            gui.setItem(33, secondaryBackground);
            gui.setItem(34, secondaryBackground);
        } else if (size == 54) {
            gui.setItem(4, description);
            gui.setItem(20, autoSell);
            gui.setItem(22, radius);
            gui.setItem(24, fragment);
            gui.setItem(0, mainBackground);
            gui.setItem(1, mainBackground);
            gui.setItem(2, mainBackground);
            gui.setItem(3, mainBackground);
            gui.setItem(5, mainBackground);
            gui.setItem(6, mainBackground);
            gui.setItem(7, mainBackground);
            gui.setItem(8, mainBackground);
            gui.setItem(9, mainBackground);
            gui.setItem(17, mainBackground);
            gui.setItem(18, mainBackground);
            gui.setItem(26, mainBackground);
            gui.setItem(27, mainBackground);
            gui.setItem(35, mainBackground);
            gui.setItem(36, mainBackground);
            gui.setItem(44, mainBackground);
            gui.setItem(45, mainBackground);
            gui.setItem(46, mainBackground);
            gui.setItem(47, mainBackground);
            gui.setItem(48, mainBackground);
            gui.setItem(49, mainBackground);
            gui.setItem(50, mainBackground);
            gui.setItem(51, mainBackground);
            gui.setItem(52, mainBackground);
            gui.setItem(53, mainBackground);
            gui.setItem(10, secondaryBackground);
            gui.setItem(11, secondaryBackground);
            gui.setItem(12, secondaryBackground);
            gui.setItem(13, secondaryBackground);
            gui.setItem(14, secondaryBackground);
            gui.setItem(15, secondaryBackground);
            gui.setItem(16, secondaryBackground);
            gui.setItem(19, secondaryBackground);
            gui.setItem(21, secondaryBackground);
            gui.setItem(23, secondaryBackground);
            gui.setItem(25, secondaryBackground);
            gui.setItem(28, secondaryBackground);
            gui.setItem(29, secondaryBackground);
            gui.setItem(30, secondaryBackground);
            gui.setItem(31, secondaryBackground);
            gui.setItem(32, secondaryBackground);
            gui.setItem(33, secondaryBackground);
            gui.setItem(34, secondaryBackground);
            gui.setItem(37, secondaryBackground);
            gui.setItem(38, secondaryBackground);
            gui.setItem(39, secondaryBackground);
            gui.setItem(40, custom);
            gui.setItem(41, secondaryBackground);
            gui.setItem(42, secondaryBackground);
            gui.setItem(43, secondaryBackground);
        }
        return gui;
    }
}