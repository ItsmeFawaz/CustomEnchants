package me.bottleofglass.CustomEnchants;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class CustomEnchantConfig {
    private static int luminationPrice;
    private static int waterBreathingPrice;
    private static int feedingPrice;
    private static int resistancePrice;
    private static int fireResistancePrice;
    private static int mobcoinPrice;
    private static int inquisitivePrice;
    private static int healthBoostPrice;
    private static int strengthPrice;
    private static int speedPrice;
    private static String insufficientFunds;
    private static String transactionSuccess;
    private static String wrongEquipment;
    private static String enchantSuccess;
    private static String existingEnchant;
    private static String shopBook;
    public static void initializeConfig(FileConfiguration config) {
        ConfigurationSection priceSection = config.getConfigurationSection("shopPrices");
        luminationPrice = priceSection.getInt("luminationPrice");
        waterBreathingPrice = priceSection.getInt("waterBreathingPrice");
        feedingPrice = priceSection.getInt("feedingPrice");
        resistancePrice = priceSection.getInt("resistancePrice");
        fireResistancePrice = priceSection.getInt("fireResistancePrice");
        mobcoinPrice = priceSection.getInt("mobcoinPrice");
        inquisitivePrice = priceSection.getInt("inquisitivePrice");
        healthBoostPrice = priceSection.getInt("healthBoostPrice");
        strengthPrice = priceSection.getInt("strengthPrice");
        speedPrice = priceSection.getInt("speedPrice");
        ConfigurationSection messageSection = config.getConfigurationSection("messages");
        insufficientFunds = messageSection.getString("insufficientFunds");
        transactionSuccess = messageSection.getString("transactionSuccess");
        wrongEquipment= messageSection.getString("wrongEquipment");
        enchantSuccess = messageSection.getString("enchantSuccess");
        existingEnchant = messageSection.getString("existingEnchant");
        shopBook = messageSection.getString("shopBook");
    }

    public static int getLuminationPrice() {
        return luminationPrice;
    }

    public static int getWaterBreathingPrice() {
        return waterBreathingPrice;
    }

    public static int getFeedingPrice() {
        return feedingPrice;
    }

    public static int getResistancePrice() {
        return resistancePrice;
    }

    public static int getFireResistancePrice() {
        return fireResistancePrice;
    }

    public static int getMobcoinPrice() {
        return mobcoinPrice;
    }

    public static int getInquisitivePrice() {
        return inquisitivePrice;
    }

    public static int getHealthBoostPrice() {
        return healthBoostPrice;
    }

    public static int getStrengthPrice() {
        return strengthPrice;
    }

    public static int getSpeedPrice() {
        return speedPrice;
    }

    public static String getInsufficientFunds() {
        return insufficientFunds;
    }

    public static String getTransactionSuccess() {
        return transactionSuccess;
    }

    public static String getWrongEquipment() {
        return wrongEquipment;
    }

    public static String getEnchantSuccess() {
        return enchantSuccess;
    }

    public static String getExistingEnchant() {
        return existingEnchant;
    }

    public static String getShopBook() {
        return shopBook;
    }
}
