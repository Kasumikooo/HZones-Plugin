package org.jellypink.HZones.listeners.player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.apache.logging.log4j.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.sk89q.worldguard.WorldGuard;
import org.bukkit.event.entity.PlayerDeathEvent;

import org.jellypink.HZones.Main;
import org.jellypink.HZones.config.MainConfigManager;
import org.jellypink.HZones.managers.PvPSystemManager;
import org.jellypink.HZones.models.ZoneFlagType;
import org.jellypink.HZones.utils.MessageUtils;

public class PlayerDeathListener implements Listener {

    private final Main plugin;

    public PlayerDeathListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PvPSystemManager pvpSystem = new PvPSystemManager(plugin);

        Location wgLocation = BukkitAdapter.adapt(player.getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(player.getWorld()));

        if (regionManager == null) return;

        ApplicableRegionSet applicableRegions = regionManager.getApplicableRegions(wgLocation.toVector().toBlockPoint());

        StateFlag.State isPluginRegion = applicableRegions.queryValue(null, Flags.SNOWMAN_TRAILS);

        if (isPluginRegion != StateFlag.State.ALLOW) {
            handleWildernessDeath(player, event);
            return;
        }

        // call methods
        ZoneFlagType PlayerZone = ZoneFlagType.getActiveZone(applicableRegions);
        String globalAlert = String.format(plugin.getMainConfigManager().getBlack_Zone_Death().replace("%s", player.getName()));
        MainConfigManager mainConfigManager = plugin.getMainConfigManager();

        if (PlayerZone != null){
            switch (PlayerZone) {
                case GREEN:
                    event.setKeepInventory(true);
                    event.setKeepLevel(true);
                    event.getDrops().clear();
                    event.setDroppedExp(0);

                    if(mainConfigManager.isGreen_Zone_DeathEnabled()){
                        player.sendMessage(MessageUtils.getColoredMessage(plugin.getMainConfigManager().getGreen_Zone_Death()));
                    }
                    break;
                case YELLOW:
                    event.setKeepInventory(true);
                    event.getDrops().clear();
                    event.setKeepLevel(false);

                    if(mainConfigManager.isYellow_Zone_DeathEnabled()){
                        player.sendMessage(MessageUtils.getColoredMessage(plugin.getMainConfigManager().getYellow_Zone_Death()));
                    }
                    break;
                case RED:
                    event.setKeepInventory(true);
                    event.getDrops().clear();
                    event.setKeepLevel(true);

                    if(mainConfigManager.isBlack_Zone_DeathEnabled()){
                        player.sendMessage(MessageUtils.getColoredMessage(plugin.getMainConfigManager().getBlack_Zone_Death()));
                    }
                    break;
                case BLACK:
                    event.setKeepInventory(false);
                    Player playerdeath = player.getPlayer();
                    org.bukkit.Location location = player.getLocation();
                    playerdeath.getWorld().strikeLightningEffect(location);

                    if(mainConfigManager.isBlack_Zone_DeathEnabled()){
                        player.sendMessage(MessageUtils.getColoredMessage(plugin.getMainConfigManager().getBlack_Zone_Death()));
                    }
                    break;
            }
        }
    }

    private void handleWildernessDeath(Player player, PlayerDeathEvent event) {
        String globalAlert = String.format(plugin.getMainConfigManager().getBlack_Zone_Death().replace("%s%", player.getName()));
        player.getServer().broadcastMessage(MessageUtils.getColoredMessage(globalAlert));
    }

}

