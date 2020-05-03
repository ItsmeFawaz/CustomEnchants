package me.bottleofglass.CustomEnchants.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ListGUI extends GUI {
    private Inventory openInventory;
    private List<GUIPage> pageList = new ArrayList<>();

    public ListGUI(Player p) { super(p);}
    public ListGUI(Player p, String title) { super(p, title);}
    public ListGUI(Player p, Collection<GUIButton> buttons) {
        super(p);
        addButtons(buttons);
    }
    //adds button the the last page
    public void addButton(GUIButton button) {
        GUIPage page;
        //if there's no page, create one and add button as the first button
        if(pageList.size() == 0) {
            page = new GUIPage(0, guiTitle, this);
            page.getButtonList().put(0, button);
            page.getInv().setItem(0, button.getStack());
            buttons.put(0, button);
            pageList.add(page);
        } else { // if there's already pages
            int index = 0;
            //find the first page with empty spots
            while(pageList.get(index).getButtonList().size()>=45) {
                index++;
                //if the last page is full, create a new one
                if(index >= pageList.size()) {
                    pageList.add(new GUIPage(index, guiTitle, this));
                    break;
                }
            }
            //add button to that page
            page = pageList.get(index);
            int buttonPos = firstEmpty(page.getButtonList());
            page.getButtonList().put(buttonPos,button);
            page.getInv().setItem(buttonPos, button.getStack());
        }
    }

    public boolean addButtons(Collection<GUIButton> buttons) {
        for(GUIButton button : buttons) {
            addButton(button);
        }
        return true;
    }

    @Override
    public void open() {
        if(pageList.size() == 0) {
            GUIPage page = new GUIPage(0, guiTitle,this);
            openInventory = page.getInv();
            owner.openInventory(openInventory);
            return;
        }
        for(GUIPage page : pageList) {
            if(page.getPageNo() < pageList.size()-1) {
                ItemStack stack;
                ItemMeta meta;
                stack = new ItemStack(Material.PAPER);
                meta =  stack.getItemMeta();
                meta.setDisplayName(ChatColor.GRAY + "Next Page");
                stack.setItemMeta(meta);
                page.getInv().setItem(50, stack);
                page.getButtonList().put(50, new GUIButton(stack, this::nextPage));
            }
        }
        openInventory = pageList.get(0).getInv();
        owner.openInventory(openInventory);
        guiList.put( owner,this);
    }
    public int firstEmpty() {
        int index = 0;
        while(buttons.get(index) != null) {
            index++;

        }
        return index;
    }
    public int firstEmpty(HashMap<Integer, GUIButton> map) {
        int index = 0;
        while(map.containsKey(index)) {
            index++;
        }
        return index;
    }
    @Override
    public GUIButton getButtonAt(int i) {
        return getButtonAt(i, 0);
    }
    public GUIButton getButtonAt(int i, int pageNo) {
        return pageList.get(pageNo).getButtonList().get(i);
    }
    @Override
    public Inventory getInventory() {
        return openInventory;
    }
    public void previousPage() {
        int pageNo = Integer.parseInt(String.valueOf(openInventory.getItem(49).getItemMeta().getDisplayName().charAt(openInventory.getItem(49).getItemMeta().getDisplayName().length()-1)))-2;
        GUIPage newPage = pageList.get(pageNo);
        openInventory = newPage.getInv();
        guiList.put(owner,this);
        owner.openInventory(openInventory);
    }
    public void nextPage() {
        int pageNo = Integer.parseInt(String.valueOf(openInventory.getItem(49).getItemMeta().getDisplayName().charAt(openInventory.getItem(49).getItemMeta().getDisplayName().length()-1)));
        GUIPage newPage = pageList.get(pageNo);
        openInventory = newPage.getInv();
        guiList.put(owner,this);
        owner.openInventory(openInventory);
    }
}
