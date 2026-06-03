package org.jellypink.HZones.managers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.antlr.v4.runtime.misc.NotNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jellypink.HZones.models.ZoneFlagType;
import org.jellypink.HZones.utils.MessageUtils;

import javax.annotation.Nullable;

import static com.sk89q.worldguard.WorldGuard.getInstance;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    public static void registerHook() {
        new PlaceholderAPIHook().register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "hyterMMO";
    }

    @Override
    public @NotNull String getAuthor() {
        return "JellyLink";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer != null && offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();

            if (params.equalsIgnoreCase("zone")) {
                Location wgLocation = BukkitAdapter.adapt(player.getLocation());

                RegionContainer container = getInstance().getPlatform().getRegionContainer();
                RegionManager regionManager = container.get(BukkitAdapter.adapt(player.getWorld()));

                if (regionManager != null) {
                    ApplicableRegionSet applicableRegions = regionManager.getApplicableRegions(wgLocation.toVector().toBlockPoint());

                    ZoneFlagType zone = ZoneFlagType.getActiveZone(applicableRegions);

                    if (zone != null) {
                        return MessageUtils.getColoredMessage(zone.getDisplayName());
                    }
                }

                return MessageUtils.getColoredMessage("&aWilderness");
            }
        }
        return null;
    }

}
