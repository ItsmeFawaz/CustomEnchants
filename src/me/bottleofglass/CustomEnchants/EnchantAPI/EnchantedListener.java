package me.bottleofglass.CustomEnchants.EnchantAPI;

import me.bottleofglass.CustomEnchants.CustomEnchantConfig;
import me.bottleofglass.CustomEnchants.Main;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagShort;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Directional;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.Dispenser;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EnchantedListener implements Listener {
    @EventHandler
    public void onKill(EntityDeathEvent evt) {
        if(evt.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            Player killer = evt.getEntity().getKiller();
            if(killer == null)
                return;
            boolean inquisitive = false;
            if(killer.getItemInHand() != null && killer.getItemInHand().hasItemMeta() && killer.getItemInHand().getItemMeta().hasLore()) {
                for(String lore : killer.getItemInHand().getItemMeta().getLore()) {
                    if(lore.startsWith("Inquisitive")) {
                        inquisitive = true;
                    }
                }
            }
            if(inquisitive)
                evt.setDroppedExp((int) Math.floor(evt.getDroppedExp()*1.5D));
        }
    }
    @EventHandler
    public void onClick(InventoryClickEvent evt) {
        if(evt.getClickedInventory() == null || evt.getInventory() == null) {
            return;
        }
        if(evt.getClickedInventory().getType() == InventoryType.ANVIL || evt.getWhoClicked().getOpenInventory().getTopInventory().getType() == InventoryType.ANVIL) {
            AnvilInventory inv = (AnvilInventory) evt.getWhoClicked().getOpenInventory().getTopInventory();
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                if(inv.getItem(2) != null && inv.getItem(2).hasItemMeta() && inv.getItem(2).getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                    ItemStack stack = inv.getItem(2);
                    ItemMeta meta = stack.getItemMeta();
                    meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                    stack.setItemMeta(meta);
                    inv.setItem(2, stack);
                }
            });
        }
        if(evt.getClick() == ClickType.RIGHT || evt.getClick() == ClickType.LEFT) {
            //if it's a enchant token
            if(EnchantToken.isValid(evt.getCursor())) {
                ItemStack clickedItem = evt.getCurrentItem();
                ItemStack itemOnCursor = evt.getCursor();
                if(clickedItem == null || itemOnCursor == null) {
                    return;
                }
                if(itemOnCursor.hasItemMeta()) {
                    String name = itemOnCursor.getItemMeta().getDisplayName();
                    String enchantName = ChatColor.stripColor(name.substring(name.indexOf('(')+1, name.length() - 1));
                    if(CustomEnchant.getByID(enchantName) != null) {
                        Player p = (Player) evt.getWhoClicked();
                        EnchantToken token = new EnchantToken(CustomEnchant.getByID(enchantName));
                        //if is valid equipment
                        if(clickedItem.getType().toString().endsWith(token.getEnch().getArmorType().toUpperCase())) {
                            evt.setCancelled(true);
                            ItemMeta meta = clickedItem.getItemMeta();
                            if(meta.hasLore()) {
                                List<String> lores = meta.getLore();
                                if(lores.contains(ChatColor.AQUA + enchantName)) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', CustomEnchantConfig.getExistingEnchant().replace("{item}", clickedItem.getType().toString()).replace("{enchant}",enchantName )));
                                    return;
                                }
                                lores.add(ChatColor.AQUA + enchantName);
                                meta.setLore(lores);
                            } else {
                                List<String> lores = new ArrayList<>();
                                lores.add(ChatColor.AQUA + enchantName);
                                meta.setLore(lores);
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', CustomEnchantConfig.getEnchantSuccess().replace("{item}", clickedItem.getType().toString()).replace("{enchant}",enchantName )));
                            //if there's no previous enchant, do a random enchant for glow effect and hide it
                            if(!meta.hasEnchants()) {
                                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            }
                            clickedItem.setItemMeta(meta);
                            if (!meta.hasEnchants()) {
                                evt.setCurrentItem(addGlow(clickedItem));
                            } else {
                                evt.setCurrentItem(clickedItem);
                            }
                            if(evt.getWhoClicked().getItemOnCursor().getAmount() > 1) {
                                evt.getWhoClicked().getItemOnCursor().setAmount(evt.getWhoClicked().getItemOnCursor().getAmount()-1);
                            } else {
                                evt.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                            }
                            return;
                        } 
                    }

                }
            }
        }
        if((evt.getCurrentItem() != null && CustomEnchant.getEnchants(evt.getCurrentItem()).size() > 0) ||
                (evt.getCursor() != null && CustomEnchant.getEnchants(evt.getCursor()).size() >0) ||
                (evt.getHotbarButton() != -1 && evt.getWhoClicked().getInventory().getItem(evt.getHotbarButton()) != null && CustomEnchant.getEnchants(evt.getWhoClicked().getInventory().getItem(evt.getHotbarButton())).size() > 0)) {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> CustomEnchant.updateEffects((Player) evt.getWhoClicked()));
            return;
        }
    }
    @EventHandler
    public void onBreak(PlayerItemBreakEvent evt) {
        String armorType = evt.getBrokenItem().getType().toString().split("_").length == 2 ? evt.getBrokenItem().getType().toString().split("_")[1] : "";
        if((armorType.equalsIgnoreCase("helmet")) ||
                (armorType.equalsIgnoreCase("chestplate")) ||
                (armorType.equalsIgnoreCase("leggings")) ||
                (armorType.equalsIgnoreCase("boots"))) {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                CustomEnchant.updateEffects(evt.getPlayer());
            });
        }
    }
    @EventHandler
    public void onDispense(BlockDispenseEvent evt) {
        if(CustomEnchant.getEnchants(evt.getItem()).size() > 0) {
            for(Entity entity :evt.getBlock().getWorld().getNearbyEntities(evt.getBlock().getLocation(), 1, 2, 1)) {
                if(entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        CustomEnchant.updateEffects(livingEntity);
                    });
                }
            }
        }
    }
    @EventHandler
    public void onClick(PlayerInteractEvent evt) {
        if(evt.getItem() == null)
            return;
        if(evt.getAction() == Action.RIGHT_CLICK_AIR || evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String armorType = evt.getItem().getType().toString().split("_").length == 2 ? evt.getItem().getType().toString().split("_")[1] : "";
            if((armorType.equalsIgnoreCase("helmet") && evt.getPlayer().getEquipment().getHelmet() == null) ||
                    (armorType.equalsIgnoreCase("chestplate") && evt.getPlayer().getEquipment().getChestplate() == null) ||
                    (armorType.equalsIgnoreCase("leggings") && evt.getPlayer().getEquipment().getLeggings() == null) ||
                    (armorType.equalsIgnoreCase("boots") && evt.getPlayer().getEquipment().getBoots() == null)) {
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    CustomEnchant.updateEffects(evt.getPlayer());
                });
            }
        }
    }
    public ItemStack addGlow(ItemStack stack) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound compound = nmsStack.getTag() != null ? nmsStack.getTag() : new NBTTagCompound();
        NBTTagList list = compound.get("ench") != null ? (NBTTagList) compound.get("ench") : new NBTTagList();
        list.add(new NBTTagCompound());
        compound.set("ench", list);
        nmsStack.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }
}
