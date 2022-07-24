package com.chup.advancedharvesterhoes.configuration;

import com.chup.advancedharvesterhoes.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigManager {
    private static final Map<String, FileConfiguration> configs = new HashMap();
    private final Main plugin;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    private static boolean isFileLoaded(String fileName) {
        return configs.containsKey(fileName);
    }

    public void load(String fileName) {
        File file = new File(this.plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                this.plugin.saveResource(fileName, false);
            } catch (Exception var4) {
                Bukkit.getLogger().log(Level.SEVERE, "Error creating file" + fileName, var4);
            }
        }

        if (!isFileLoaded(fileName)) {
            configs.put(fileName, YamlConfiguration.loadConfiguration(file));
        }

    }

    public static FileConfiguration get(String fileName) {
        return isFileLoaded(fileName) ? (FileConfiguration)configs.get(fileName) : null;
    }

    public static boolean update(String fileName, String path, Object value) {
        if (isFileLoaded(fileName) && !((FileConfiguration)configs.get(fileName)).contains(path)) {
            ((FileConfiguration)configs.get(fileName)).set(path, value);
            return true;
        } else {
            return false;
        }
    }

    public static void set(String fileName, String path, Object value) {
        if (isFileLoaded(fileName)) {
            ((FileConfiguration)configs.get(fileName)).set(path, value);
        }

    }

    public static void remove(String fileName, String path) {
        if (isFileLoaded(fileName)) {
            ((FileConfiguration)configs.get(fileName)).set(path, (Object)null);
        }

    }

    public static boolean contains(String fileName, String path) {
        return isFileLoaded(fileName) ? ((FileConfiguration)configs.get(fileName)).contains(path) : false;
    }

    public void reload(String fileName) {
        File file = new File(this.plugin.getDataFolder(), fileName);
        if (isFileLoaded(fileName)) {
            try {
                ((FileConfiguration)configs.get(fileName)).load(file);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    }

    public void save(String fileName) {
        File file = new File(this.plugin.getDataFolder(), fileName);
        if (isFileLoaded(fileName)) {
            try {
                ((FileConfiguration)configs.get(fileName)).save(file);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    }

    /** @deprecated */
    public void addComment(String fileName, String path, String... comments) {
        if (isFileLoaded(fileName)) {
            String[] var4 = comments;
            int var5 = comments.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String comment = var4[var6];
                if (!((FileConfiguration)configs.get(fileName)).contains(path)) {
                    ((FileConfiguration)configs.get(fileName)).set("_COMMENT_" + comments.length, " " + comment);
                }
            }
        }

    }
}
