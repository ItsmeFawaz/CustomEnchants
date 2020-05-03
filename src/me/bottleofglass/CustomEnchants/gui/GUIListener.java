package me.bottleofglass.CustomEnchants.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent evt) {
        GUI toRemove = null;
        if(GUI.getGuiList().containsKey(evt.getWhoClicked())) {
            GUI gui = GUI.getGuiList().get(evt.getWhoClicked());
            if(gui.getInventory().equals(evt.getClickedInventory())) {
                evt.setCancelled(true);
                if(gui instanceof ListGUI) {
                    ListGUI listGUI = (ListGUI) gui;
                    int pageNo = Integer.parseInt(String.valueOf(evt.getInventory().getItem(49).getItemMeta().getDisplayName().charAt(evt.getInventory().getItem(49).getItemMeta().getDisplayName().length()-1)))-1;
                    if(listGUI.getButtonAt(evt.getSlot(), pageNo) != null) {
                        gui.close();
                        toRemove = gui;
                        listGUI.getButtonAt(evt.getSlot(), pageNo).action();
                    }
                }
                if(gui.getButtonAt(evt.getSlot()) != null && gui.getButtonAt(evt.getSlot()).hasAction()) {
                    gui.getButtonAt(evt.getSlot()).action();
                }
            }
        }
        if(toRemove != null) GUI.getGuiList().remove(evt.getWhoClicked());

    }
    @EventHandler
    public void onClose(InventoryCloseEvent evt) {
        if(GUI.getGuiList().containsKey(evt.getPlayer())) {
            GUI gui = GUI.getGuiList().get(evt.getPlayer());
            if(gui.getInventory().equals(evt.getInventory())) {
                GUI.getGuiList().remove(gui);
                return;
            }
        }
    }
}
