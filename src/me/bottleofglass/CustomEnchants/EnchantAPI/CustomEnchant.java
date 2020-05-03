package me.bottleofglass.CustomEnchants.EnchantAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

public enum CustomEnchant {
    LUMINATION("Lumination I","Helmet", PotionEffectType.NIGHT_VISION.createEffect(Integer.MAX_VALUE, 0)),
    WATER_BREATHING("Water Breathing I","Helmet", PotionEffectType.WATER_BREATHING.createEffect(Integer.MAX_VALUE,0)),
    FEEDING("Feeding I","Helmet", new PotionEffect(PotionEffectType.SATURATION,99999999, 0)),
    RESISTANCE("Resistance I","Chestplate", PotionEffectType.DAMAGE_RESISTANCE.createEffect(Integer.MAX_VALUE, 0)),
    FIRE_RESISTANCE("Fire Resistance I","Leggings", PotionEffectType.FIRE_RESISTANCE.createEffect(Integer.MAX_VALUE,0)),
    MOBCOIN("Mobcoin I","Sword", null),
    INQUISITIVE("Inquisitive I","Sword", null),
    HEALTH_BOOST_2("Health Boost I","Chestplate", PotionEffectType.HEALTH_BOOST.createEffect(Integer.MAX_VALUE,0)),
    STRENGTH_2("Strength II","Chestplate", PotionEffectType.INCREASE_DAMAGE.createEffect(Integer.MAX_VALUE,1)),
    SPEED_2("Speed II","Boots",PotionEffectType.SPEED.createEffect(Integer.MAX_VALUE,1));

    private final String equipmentType;
    private final String ID;
    private final PotionEffect effect;
    CustomEnchant(String name, String armorType, PotionEffect effect) {
        this.equipmentType = armorType;
        this.ID = name;
        this.effect = effect;
    }

    public String getID() { return ID;}
    public PotionEffect getEffect() {return effect;}
    public String getArmorType() {return equipmentType;}

    public static CustomEnchant getByID(String ID) {
        Optional<CustomEnchant> enchant = Arrays.stream(CustomEnchant.values()).filter(x -> x.getID().equalsIgnoreCase(ID)).findFirst();
        if(enchant.isPresent()) {
            return enchant.get();
        }
        return null;
    }

    public static Set<CustomEnchant> getEnchants(ItemStack stack) {
        Set<CustomEnchant> enchants = new HashSet<>();
        if(stack.hasItemMeta() && stack.getItemMeta().hasLore()) {
            List<String> lores = stack.getItemMeta().getLore();
            for(CustomEnchant ench : CustomEnchant.values()) {
                if(lores.contains(ChatColor.AQUA +ench.getID()))
                    enchants.add(ench);
            }
        }
        return enchants;
    }
    public static void addCustomEnchant(ItemStack stack,CustomEnchant enchant) {
        List<String> lores;
        ItemMeta meta;
        if(getEnchants(stack).contains(enchant))
            return;
        if(stack.hasItemMeta()) {
            meta = stack.getItemMeta();
            if(stack.getItemMeta().hasLore()) {
                lores = stack.getItemMeta().getLore();
            } else {
                lores = new ArrayList<>();
            }
        }  else {
            meta = Bukkit.getItemFactory().getItemMeta(stack.getType());
            lores = new ArrayList<>();
        }
        lores.add(ChatColor.AQUA + enchant.getID());
        meta.setLore(lores);
        stack.setItemMeta(meta);
    }
    public static ItemStack addCustomEnchants(ItemStack stack, Set<CustomEnchant> enchantList) {
        Set<CustomEnchant> existingEnchants = getEnchants(stack);
        enchantList.removeAll(existingEnchants);
        List<String> lores;
        ItemMeta meta;
        if(stack.hasItemMeta()) {
            meta = stack.getItemMeta();
            if(stack.getItemMeta().hasLore()) {
                lores = stack.getItemMeta().getLore();
            } else {
                lores = new ArrayList<>();
            }
        }  else {
            meta = Bukkit.getItemFactory().getItemMeta(stack.getType());
            lores = new ArrayList<>();
        }
        for(CustomEnchant ench : enchantList) {
            lores.add(ChatColor.AQUA + ench.getID());
        }
        meta.setLore(lores);
        stack.setItemMeta(meta);
        return stack;
    }
    public static boolean updateEffects(LivingEntity p) {
        HashMap<PotionEffectType, CustomEnchant> enchants = new HashMap<>();
        for(ItemStack stack : p.getEquipment().getArmorContents()) {
            for(CustomEnchant ench: getEnchants(stack)) {
                enchants.put(ench.getEffect().getType(), ench);
            }
        }
        for(PotionEffect effect : p.getActivePotionEffects()) {
            if((!enchants.containsKey(effect.getType())) && effect.getDuration() > 100000) {
                p.removePotionEffect(effect.getType());
            }
        }
        if(enchants.size() != 0) {
            for(CustomEnchant ench: enchants.values()) {
                p.addPotionEffect(ench.getEffect(), true);

            }
            return true;
        } else {
            return false;
        }

    }
}