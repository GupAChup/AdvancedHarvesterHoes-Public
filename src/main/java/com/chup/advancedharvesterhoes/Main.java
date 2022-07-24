package com.chup.advancedharvesterhoes;

import com.chup.advancedharvesterhoes.configuration.ConfigManager;
import com.chup.advancedharvesterhoes.extras.Metrics;
import com.chup.advancedharvesterhoes.listeners.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    public static Economy econ;
    public ConfigManager configManager;

    @Override
    public void onEnable(){

        System.out.println("AdvancedHarvesterHoes >> Plugin successfully enabled.");

        if(!setupEconomy()) {
            System.out.println("AdvancedHarvesterHoes >> Error: No economy plugin detected!");
        }

        this.configManager = new ConfigManager(this);
        this.configManager.load("messages.yml");
        this.configManager.save("messages.yml");

        new Config(this);
        new UpgradeGUI(this);
        new Extras(this);

        Metrics metrics = new Metrics(this, 9684);

        getCommand("harvesterhoe").setExecutor(new HarvesterHoeExecutor(this));
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