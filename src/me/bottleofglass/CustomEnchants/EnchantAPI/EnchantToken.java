package me.bottleofglass.CustomEnchants.EnchantAPI;

import me.bottleofglass.CustomEnchants.CustomEnchantConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantToken {
    private ItemStack stack;
    private CustomEnchant ench;
    private int price;
    public EnchantToken(CustomEnchant enchantType) {
        ench = enchantType;
        stack = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Enchant Crystal &7(&b" + enchantType.getID() +"&7)"));
        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.DARK_AQUA + "Drag and drop this item on a ");
        lores.add(ChatColor.AQUA  + getEnch().getArmorType() + ChatColor.DARK_AQUA +" to place the enchantment");
        meta.setLore(lores);
        stack.setItemMeta(meta);
        switch(getEnch()) {
            case LUMINATION:
                price = CustomEnchantConfig.getLuminationPrice();
                break;
            case WATER_BREATHING:
                price = CustomEnchantConfig.getWaterBreathingPrice();
                break;
            case FEEDING:
                price = CustomEnchantConfig.getFeedingPrice();
                break;
            case RESISTANCE:
                price = CustomEnchantConfig.getResistancePrice();
                break;
            case FIRE_RESISTANCE:
                price = CustomEnchantConfig.getFireResistancePrice();
                break;
            case MOBCOIN:
                price = CustomEnchantConfig.getMobcoinPrice();
                break;
            case INQUISITIVE:
                price = CustomEnchantConfig.getInquisitivePrice();
                break;
            case HEALTH_BOOST_2:
                price = CustomEnchantConfig.getHealthBoostPrice();
                break;
            case STRENGTH_2:
                price = CustomEnchantConfig.getStrengthPrice();
                break;
            case SPEED_2:
                price = CustomEnchantConfig.getSpeedPrice();
                break;
            default:
                price = 0;
        }
    }
    public ItemStack getStack() {
        return stack;
    }
    public int getPrice() {
        return price;
    }
    public static boolean isValid(ItemStack stack) {
        if(stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            if(meta.hasLore()) {
                return ChatColor.stripColor(meta.getDisplayName()).startsWith("Enchant Crystal") && ChatColor.stripColor(meta.getLore().get(0)).startsWith("Drag and drop this item on a");
            }
        }
        return false;
    }

    public CustomEnchant getEnch() {
        return ench;
    }
}
