package me.bottleofglass.CustomEnchants.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

public class ConfirmationGUI extends CustomizedGUI {

    public ConfirmationGUI(Player p) {
        super(p, 3, ChatColor.RED + "Are you sure?");
        ItemStack confirmStack = new Wool(DyeColor.GREEN).toItemStack();
        confirmStack.setAmount(1);
        ItemMeta confirmMeta = confirmStack.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "Confirm");
        confirmStack.setItemMeta(confirmMeta);
        ItemStack denyStack = new Wool(DyeColor.RED).toItemStack();
        denyStack.setAmount(1);
        ItemMeta denyMeta = denyStack.getItemMeta();
        denyMeta.setDisplayName(ChatColor.DARK_RED + "Deny");
        denyStack.setItemMeta(denyMeta);
        GUIButton denyButton = new GUIButton(denyStack);
        denyButton.setAction(() -> {
            this.close();
        });
        addButton(15, denyButton);
        addButton(11, new GUIButton(confirmStack));
    }
    public void setConfirmAction(Runnable runnable) {
        getButtonAt(11).setAction(runnable);
    }

}
