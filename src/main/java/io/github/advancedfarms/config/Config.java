package io.github.advancedfarms.config;

import io.github.advancedfarms.AdvancedFarms;

public class Config {
    private static AdvancedFarms main;

    public Config(AdvancedFarms main) {

        Config.main = main;
        main.getConfig().options().copyDefaults();
        main.saveDefaultConfig();

    }

    public static Integer getLevelBreak(Integer level) {return main.getConfig().getInt("levels." + level.toString() + ".next_level_break");}
    public static double getLevelMultiplier(Integer level) {return main.getConfig().getDouble("levels." + level.toString() + ".multiplier");}

    public static boolean getSoundStatus() {return main.getConfig().getBoolean("sound-on-break");}
    public static boolean getLevelUpMessageStatus() {return main.getConfig().getBoolean("announce-on-level-up");}
    public static double getFragmentChance() {return main.getConfig().getDouble("fragment-drop-chance");}

    public static int getAutoSellCostMoney() {return main.getConfig().getInt("upgrades.autosell.cost_money");}
    public static int getAutoSellCostFragments() {return main.getConfig().getInt("upgrades.autosell.cost_fragments");}
    public static int getCaneSellPrice() {return main.getConfig().getInt("cane-sell-price");}

    public static int getRadiusCostMoney(Integer upgrade) {return main.getConfig().getInt("upgrades.radius." + upgrade + ".cost_money");}
    public static int getRadiusCostFragments(Integer upgrade) {return main.getConfig().getInt("upgrades.radius." + upgrade + ".cost_fragments");}

    public static int getFragmentMultiplierCostMoney(Integer upgrade) {return main.getConfig().getInt("upgrades.fragment_multiplier." + upgrade + ".cost_money");}
    public static int getFragmentMultiplierCostFragments(Integer upgrade) {return main.getConfig().getInt("upgrades.fragment_multiplier." + upgrade + ".cost_fragments");}
    public static double getFragmentMultiplierMultiplier(Integer upgrade) {return main.getConfig().getDouble("upgrades.fragment_multiplier." + upgrade + ".fragment_multiplier");}

    public static boolean containsPath(Integer level) {return main.getConfig().contains("levels." + level.toString() + ".next_level_break"); }

    public static String getMainColor() {return main.getConfig().getString("gui-main-color");}
    public static String getSecondaryColor() {return main.getConfig().getString("gui-secondary-color");}

    public static boolean getCustomUpgradeStatus() {return main.getConfig().getBoolean("upgrades.custom.enabled");}
    public static int getCustomCostMoney(Integer upgrade) {return main.getConfig().getInt("upgrades.custom." + upgrade + ".cost_money");}
    public static int getCustomCostFragments(Integer upgrade) {return main.getConfig().getInt("upgrades.custom." + upgrade + ".cost_fragments");}
    public static double getCustomMultiplier(Integer upgrade) {return main.getConfig().getDouble("upgrades.custom." + upgrade + ".percentage_multiplier");}
    public static double getCustomChance() {return main.getConfig().getDouble("upgrades.custom.default_percentage");}

    public static int getCaneBeforeSell() {return main.getConfig().getInt("cane-before-sell");}

}