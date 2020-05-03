package me.bottleofglass.CustomEnchants.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.HashMap;

public class CustomizedGUI extends GUI {
    Inventory inventory;
    public CustomizedGUI(int rows, String title) {
        inventory = Bukkit.createInventory(null, rows*9,title);
    }
    public CustomizedGUI(Player p, int rows) {
        super(p);
        inventory = Bukkit.createInventory(p, rows*9);
    }
    public CustomizedGUI(Player p, int rows, String title) {
        super(p);
        inventory = Bukkit.createInventory(p, rows*9, title);
    }

    public boolean addButton(int i, GUIButton button) {
        if(i < inventory.getSize() && !buttons.containsKey(i)) {
            buttons.put(i,button);
            inventory.setItem(i, button.getStack());
            return true;
        }
        return false;
    }
    public void open(Player p) {
        owner = p;
        owner.openInventory(inventory);
        guiList.put(p, this);
    }
    public void open() {
        owner.openInventory(inventory);
        guiList.put(owner,this);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
