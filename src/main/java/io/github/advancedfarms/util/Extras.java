package io.github.advancedfarms.util;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import io.github.advancedfarms.AdvancedFarms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Extras {
    private static AdvancedFarms plugin;

    public Extras(AdvancedFarms plugin) {
        this.plugin = plugin;
    }

    public static ItemStack getItemInHand(Player p) {
        return p.getItemInHand();
    }

    public static ItemStack harvesterHoe() {
        String sugarcaneNameColor;
        String sugarcaneMainColor;
        String sugarcaneSecondaryColor;
        String sugarcaneValueColor;

        if (plugin.getMessages().contains("tool")) {
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

        ItemStack harvesterHoe = new ItemStack(Material.DIAMOND_HOE);
        harvesterHoe.addUnsafeEnchantment(Enchantment.UNBREAKING, 100);
        harvesterHoe.addUnsafeEnchantment(Enchantment.EFFICIENCY, 100);
        ItemMeta harvesterHoeMeta = harvesterHoe.getItemMeta();
        harvesterHoeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', sugarcaneNameColor + "&lHarvester Hoe") + ChatColor.GRAY + " (Level 1)");

        List<String> harvesterLore = new ArrayList<>();
        harvesterLore.add("");
        harvesterLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Radius: " + sugarcaneValueColor + "(1x1)"));
        harvesterLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Harvested: " + sugarcaneValueColor + "0"));
        harvesterLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Fragments Harvested: " + sugarcaneValueColor + "0"));
        harvesterLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "&l* " + sugarcaneMainColor + "Cane Multiplier: " + sugarcaneValueColor + "1.0x"));
        harvesterLore.add("");
        harvesterLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneSecondaryColor + "Level 1" + sugarcaneValueColor + "  ||||||||||||||||||||||||||||||||||||||||  " + sugarcaneSecondaryColor + "Level 2"));
        harvesterLore.add("");
        harvesterLore.add(ChatColor.translateAlternateColorCodes('&', sugarcaneValueColor + "&o(Right-Click to open upgrades menu)"));
        harvesterHoeMeta.setLore(harvesterLore);
        harvesterHoeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        harvesterHoeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        harvesterHoe.setItemMeta(harvesterHoeMeta);

        NBTItem item = new NBTItem(harvesterHoe);
        item.setBoolean("AutoSellStatus", false);
        item.setInteger("FragMultiplier", 0);
        item.setInteger("CustomMultiplier", 0);
        return item.getItem();
    }


    public static ItemStack caneFragment(Player player) {
        ItemStack fragment = getCustomSkull(player);
        SkullMeta fragmentMeta = (SkullMeta) fragment.getItemMeta();
        fragmentMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("fragment.name")));
        List<String> fragmentLore = new ArrayList<String>();

        Iterator loreIterator = plugin.getMessages().getStringList("fragment.lore").iterator();
        while (loreIterator.hasNext()) {
            String lore = (String) loreIterator.next();
            fragmentLore.add(ChatColor.translateAlternateColorCodes('&', lore));
        }

        fragmentMeta.setLore(fragmentLore);
        fragment.setItemMeta(fragmentMeta);
        return fragment;
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server."
                    + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void send(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        try {
            Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + title + "\"}");
            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
                    int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle,
                    fadeInTime, showTime, fadeOutTime);

            Object chatsTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + subtitle + "\"}");
            Constructor<?> timingTitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
                    int.class, int.class, int.class);
            Object timingPacket = timingTitleConstructor.newInstance(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatsTitle,
                    fadeInTime, showTime, fadeOutTime);

            sendPacket(player, packet);
            sendPacket(player, timingPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ItemStack getColor(String color) {
        if (color.equalsIgnoreCase("black")) {
            return XMaterial.BLACK_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("red")) {
            return XMaterial.RED_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("green")) {
            return XMaterial.GREEN_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("brown")) {
            return XMaterial.BROWN_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("blue")) {
            return XMaterial.BLUE_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("purple")) {
            return XMaterial.PURPLE_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("cyan")) {
            return XMaterial.CYAN_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("light gray") || color.equalsIgnoreCase("light_gray") || color.equalsIgnoreCase("lightgray")) {
            return XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("gray")) {
            return XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("pink")) {
            return XMaterial.PINK_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("lime")) {
            return XMaterial.LIME_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("yellow")) {
            return XMaterial.YELLOW_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("light blue") || color.equalsIgnoreCase("light_blue") || color.equalsIgnoreCase("lightblue")) {
            return XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("magenta")) {
            return XMaterial.MAGENTA_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("orange")) {
            return XMaterial.ORANGE_STAINED_GLASS_PANE.parseItem();
        } else if (color.equalsIgnoreCase("white")) {
            return XMaterial.WHITE_STAINED_GLASS_PANE.parseItem();
        }
        return XMaterial.GLASS_PANE.parseItem();
    }

    public static ItemStack getCustomSkull(Player player) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1, (byte) 1);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(player.getName());
        stack.setItemMeta(meta);
        int max = stack.getType().getMaxStackSize();
        if (max > 1) {
            stack.setAmount(1);
        }
        return stack;
    }
}

