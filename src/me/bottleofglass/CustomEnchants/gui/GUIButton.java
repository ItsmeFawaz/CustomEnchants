package me.bottleofglass.CustomEnchants.gui;

import org.bukkit.inventory.ItemStack;

public class GUIButton {
    ItemStack stack;
    Runnable runnable;
    public GUIButton(ItemStack item) {
        stack = item;
    }
    public GUIButton(ItemStack item, Runnable runnable) {
        stack = item;
        this.runnable = runnable;
    }
    public void setAction(Runnable runnable) {
        this.runnable = runnable;
    }
    public void action() {
        if(runnable != null) {
            runnable.run();
        }
    }
    public boolean hasAction() {
        return runnable != null;
    }
    public ItemStack getStack() {return stack;}
}
