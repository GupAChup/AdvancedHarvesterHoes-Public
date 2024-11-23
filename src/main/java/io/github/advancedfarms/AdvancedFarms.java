package io.github.advancedfarms;

import io.github.advancedfarms.commands.CommandHook;
import io.github.advancedfarms.config.Config;
import io.github.advancedfarms.config.ConfigManager;
import io.github.advancedfarms.gui.UpgradeGUI;
import io.github.advancedfarms.listener.CaneListener;
import io.github.advancedfarms.listener.ClickListener;
import io.github.advancedfarms.listener.PlaceListener;
import io.github.advancedfarms.listener.RightClickListener;
import io.github.advancedfarms.util.Extras;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedFarms extends JavaPlugin {

    public static Economy econ;
    public static ConfigManager configManager;

    @Override
    public void onEnable(){
        Bukkit.getLogger().fine("Enabled plugin successfully.");
        if(!setupEconomy()) {
            Bukkit.getLogger().fine("Unable to find plugin dependency.");
        }

        this.configManager = new ConfigManager(this);
        this.configManager.load("messages.yml");
        this.configManager.save("messages.yml");

        new Config(this);
        new UpgradeGUI(this);
        new Extras(this);

        getCommand("farm").setExecutor(new CommandHook(this));
        Bukkit.getPluginManager().registerEvents(new CaneListener(this), this);
        Bukkit.getPluginManager().registerEvents(new RightClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlaceListener(this), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public FileConfiguration getMessages() {
        return this.configManager.get("messages.yml");
    }
}