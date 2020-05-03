package me.bottleofglass.CustomEnchants.commands;

import me.bottleofglass.CustomEnchants.CustomEnchantConfig;
import me.bottleofglass.CustomEnchants.EnchantAPI.CustomEnchant;
import me.bottleofglass.CustomEnchants.EnchantAPI.EnchantToken;
import me.bottleofglass.CustomEnchants.gui.CustomizedGUI;
import me.bottleofglass.CustomEnchants.gui.GUIButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomEnchantShop implements CommandExecutor {
    private CustomizedGUI shopGUI;
    public CustomEnchantShop() {

        shopGUI = new CustomizedGUI(4, "Azrael Enchants");
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',CustomEnchantConfig.getShopBook()));
        book.setItemMeta(bookMeta);
        shopGUI.addButton(0,new GUIButton(book));
        int index = 10;
        for(CustomEnchant ench : CustomEnchant.values()) {
            if(index == 24) {
                break;
            }
            if(index == 17) {
                index = 21;
            }
            EnchantToken token = new EnchantToken(ench);
            ItemStack stack = token.getStack().clone();
            ItemMeta meta = stack.getItemMeta();
            List<String> lores = meta.getLore();
            lores.add((ChatColor.translateAlternateColorCodes('&', "&7You can purchase this item for &a" + token.getPrice() + " &7levels")));
            meta.setLore(lores);
            stack.setItemMeta(meta);
            GUIButton button = new GUIButton(stack);
            button.setAction(()-> {
                if(shopGUI.getOwner().getLevel() >= token.getPrice()) {
                    shopGUI.getOwner().setLevel(shopGUI.getOwner().getLevel() - token.getPrice());
                    shopGUI.getOwner().getInventory().addItem(token.getStack());
                    shopGUI.getOwner().sendMessage(ChatColor.translateAlternateColorCodes('&', CustomEnchantConfig.getTransactionSuccess().replace("{name}", meta.getDisplayName()).replace("{price}", String.valueOf(token.getPrice()))));
                } else {
                    shopGUI.getOwner().sendMessage(ChatColor.translateAlternateColorCodes('&', CustomEnchantConfig.getInsufficientFunds().replace("{name}", meta.getDisplayName()).replace("{price}", String.valueOf(token.getPrice()))));
                }
            });
            shopGUI.addButton(index, button);
            index++;
        }
        for(int i = 0; i < 36; i++) {
            if (shopGUI.getButtonAt(i) == null) {
                ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE);
                stack.setDurability((short) 1);
                ItemMeta meta = stack.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.SILK_TOUCH,1, true);
                meta.setDisplayName(" ");
                stack.setItemMeta(meta);
                GUIButton button = new GUIButton(stack);
                shopGUI.addButton(i,button);
            }
        }
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED +"Only players may execute this command!");
            return true;
        }
        Player p = (Player) commandSender;
        shopGUI.open(p);
        return true;
    }
}
