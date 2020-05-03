package me.swanis.mobcoins.listeners;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import java.util.Random;
import me.swanis.mobcoins.Configuration;
import me.swanis.mobcoins.MobCoins;
import me.swanis.mobcoins.chance.DropChance;
import me.swanis.mobcoins.events.MobCoinsReceiveEvent;
import me.swanis.mobcoins.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class EntityListener implements Listener {
    private MobCoins instance;

    public EntityListener(MobCoins instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent event) {
        if (Configuration.DISABLED_WORLDS.contains(event.getEntity().getWorld().getName()))
            return;
        if (Configuration.MOBCOINS_ONLY_FROM_NATURALLY_SPAWNED_MOBS && !event.getEntity().hasMetadata("naturallySpawned"))
            return;
        Player killer = event.getEntity().getKiller();
        if (killer == null)
            return;
        Profile profile = this.instance.getProfileManager().getProfile(killer.getUniqueId());
        DropChance dropChance = this.instance.getChanceManager().getChance(event.getEntityType());
        if (dropChance == null)
            return;
        Random random = new Random();
        if (this.instance.hasWildStacker() && Configuration.STACKING_SUPPORT &&
                WildStackerAPI.getStackedEntity(event.getEntity()) != null) {
            int stackedAmount = WildStackerAPI.getEntityAmount(event.getEntity());
            int amount = 0;
            for (int i = 0; i < stackedAmount; i++) {
                if (random.nextInt(100) <= dropChance.getChance() - 1)
                    amount++;
            }
            if (amount == 0)
                return;
            MobCoinsReceiveEvent mobCoinsReceiveEvent1 = new MobCoinsReceiveEvent(profile, amount);
            this.instance.getServer().getPluginManager().callEvent((Event)mobCoinsReceiveEvent1);
            return;
        }
        boolean mobcoins = false;
        if(killer.getItemInHand().hasItemMeta() && killer.getItemInHand().getItemMeta().hasLore()) {
            for(String lore : killer.getItemInHand().getItemMeta().getLore()) {
                if (lore.startsWith("Mobcoins")) {
                    mobcoins = true;
                    break;
                }
            }
        }
        if (random.nextInt(100) > (dropChance.getChance()+ (mobcoins ? 5:0) - 1)) {
            return;
        }
        MobCoinsReceiveEvent mobCoinsReceiveEvent = new MobCoinsReceiveEvent(profile, 1);
        this.instance.getServer().getPluginManager().callEvent((Event)mobCoinsReceiveEvent);
    }


    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER)
            return;
        entity.setMetadata("naturallySpawned", (MetadataValue)new FixedMetadataValue((Plugin)this.instance, Boolean.valueOf(true)));
    }
}