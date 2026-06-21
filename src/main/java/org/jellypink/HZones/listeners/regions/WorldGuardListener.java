package org.jellypink.HZones.listeners.regions;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jellypink.HZones.config.MainConfigManager;
import org.jellypink.HZones.models.ZoneFlagType;

public class WorldGuardListener implements Listener {

    public static ZoneFlagType getZone(Location location) {

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        RegionQuery query = container.createQuery();

        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(location));

        return ZoneFlagType.getActiveZone(regions);
    }

    private final java.util.HashMap<java.util.UUID, ZoneFlagType> lastKnownZone = new java.util.HashMap<>();

    public ZoneFlagType getPlayerZone(Player player) {
        return getZoneFlagType(player);
    }

    @NonNull
    public static ZoneFlagType getZoneFlagType(Player player) {
        Location loc = player.getLocation();
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(loc));

        for (ZoneFlagType zone : ZoneFlagType.values()) {
            StateFlag worldGuardFlag = zone.getFlag();

            if (worldGuardFlag != null) {
                if (set.queryState(null, worldGuardFlag) == StateFlag.State.ALLOW) {
                    return zone;
                }
            }
        }
        return ZoneFlagType.BLACK;
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {
        if (event.getTo() == null || (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ())) {
            return;
        }

        Player player = event.getPlayer();
        ZoneFlagType currentZone = getPlayerZone(player);
        ZoneFlagType previousZone = lastKnownZone.get(player.getUniqueId());

        if (previousZone == currentZone) {
            return;
        }

        lastKnownZone.put(player.getUniqueId(), currentZone);

        if((currentZone == ZoneFlagType.RED || previousZone == ZoneFlagType.BLACK)) {
            if(!)
            player.sendTitle(,, 10, 20, 10);
        }
    }
}