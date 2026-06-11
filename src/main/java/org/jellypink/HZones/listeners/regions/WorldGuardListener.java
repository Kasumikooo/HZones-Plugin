package org.jellypink.HZones.listeners.regions;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.jellypink.HZones.models.ZoneFlagType;

public class WorldGuardListener implements Listener {

    public static ZoneFlagType getZone(Location location) {

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        RegionQuery query = container.createQuery();

        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(location));

        return ZoneFlagType.getActiveZone(regions);
    }

}
