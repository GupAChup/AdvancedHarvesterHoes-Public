package io.github.advancedfarms.config;

import io.github.advancedfarms.AdvancedFarms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private final Map<String, FileConfiguration> configs = new HashMap<>();
    private final AdvancedFarms plugin;

    public ConfigManager(AdvancedFarms plugin) {
        this.plugin = plugin;
    }

    private boolean isFileLoaded(String fileName) {
        return configs.containsKey(fileName);
    }

    public void load(String fileName) {
        File file = new File(this.plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                this.plugin.saveResource(fileName, false);
            } catch (Exception e) {
                Bukkit.getLogger().severe("Unable to create config: " + fileName);
            }
        }

        if (!isFileLoaded(fileName)) {
            configs.put(fileName, YamlConfiguration.loadConfiguration(file));
        }
    }

    public FileConfiguration get(String fileName) {
        return isFileLoaded(fileName) ? configs.get(fileName) : null;
    }

    public boolean update(String fileName, String path, Object value) {
        if (isFileLoaded(fileName) && !configs.get(fileName).contains(path)) {
            configs.get(fileName).set(path, value);
            return true;
        }
        return false;
    }

    public void set(String fileName, String path, Object value) {
        if (isFileLoaded(fileName)) {
            configs.get(fileName).set(path, value);
        }
    }

    public void remove(String fileName, String path) {
        if (isFileLoaded(fileName)) {
            configs.get(fileName).set(path, null);
        }
    }

    public boolean contains(String fileName, String path) {
        return isFileLoaded(fileName) && configs.get(fileName).contains(path);
    }

    public void reload(String fileName) {
        File file = new File(this.plugin.getDataFolder(), fileName);
        if (isFileLoaded(fileName)) {
            try {
                configs.get(fileName).load(file);
            } catch (Exception e) {
                Bukkit.getLogger().severe("Error reloading config: " + fileName);
                e.printStackTrace();
            }
        }
    }

    public void save(String fileName) {
        File file = new File(this.plugin.getDataFolder(), fileName);
        if (isFileLoaded(fileName)) {
            try {
                configs.get(fileName).save(file);
            } catch (Exception e) {
                Bukkit.getLogger().severe("Error saving config: " + fileName);
                e.printStackTrace();
            }
        }
    }
}
