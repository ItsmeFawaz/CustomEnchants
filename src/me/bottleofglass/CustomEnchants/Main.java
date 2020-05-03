package me.bottleofglass.CustomEnchants;

import me.bottleofglass.CustomEnchants.EnchantAPI.EnchantedListener;
import me.bottleofglass.CustomEnchants.commands.CustomEnchantShop;
import me.bottleofglass.CustomEnchants.gui.GUIListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    boolean wildStacker;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadDependencies();
        CustomEnchantConfig.initializeConfig(getConfig());
        getCommand("ce").setExecutor(new CustomEnchantShop());
        getServer().getPluginManager().registerEvents(new EnchantedListener(), this);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
    }
    public static Main getInstance() {return instance;}
    private void loadDependencies() {
        if (getServer().getPluginManager().getPlugin("WildStacker") != null)
            this.wildStacker = true;
    }
    public boolean hasWildStacker() {return wildStacker;}
}
