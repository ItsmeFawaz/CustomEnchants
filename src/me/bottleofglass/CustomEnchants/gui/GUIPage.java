package me.bottleofglass.CustomEnchants.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class GUIPage {
    private int pageNo;
    private Inventory inv;
    private HashMap<Integer, GUIButton> buttonList = new HashMap<>();
    private ListGUI listGUI;

    public GUIPage(int pageNo, String title, ListGUI gui) {
        listGUI = gui;
        inv = Bukkit.createInventory(null, 54, title);
        this.pageNo = pageNo;
        ItemStack stack = new ItemStack(Material.ITEM_FRAME);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Page #" + (pageNo+1));
        stack.setItemMeta(meta);
        inv.setItem(49, stack);
        if(pageNo > 0) {
            stack = new ItemStack(Material.PAPER);
            meta =  stack.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "Previous Page");
            stack.setItemMeta(meta);
            inv.setItem(48, stack);
            buttonList.put(48, new GUIButton(stack, () -> {
               listGUI.previousPage();
            }));
        }
    }
    public Inventory getInv() {return inv;}
    public ListGUI getListGUI() {return listGUI;}
    public HashMap<Integer, GUIButton> getButtonList() { return buttonList;}
    public int getPageNo() {return pageNo;}
}