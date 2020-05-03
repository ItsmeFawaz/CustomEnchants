package me.bottleofglass.CustomEnchants.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;

public abstract class GUI  implements Cloneable{
    protected static HashMap<Player, GUI> guiList = new HashMap<>();
    protected HashMap<Integer, GUIButton> buttons = new HashMap<>();
    protected String guiTitle = "";
    protected Player owner;
    public GUI() {}
    public GUI(Player p) {
        owner = p;
    }
    public GUI(Player p,String title) {
        guiTitle = title;
        owner = p;
    }
    public abstract void open();
    public GUIButton getButtonAt(int i) {
        return buttons.get(i);
    }
    public static HashMap<Player, GUI> getGuiList() {
        return guiList;
    }
    public abstract Inventory getInventory();
    public void close() {
        owner.closeInventory();
        guiList.remove(this);
    }
    public HashMap<Integer, GUIButton> getButtons() {return buttons;}
    public Player getOwner() {return owner;}
}
