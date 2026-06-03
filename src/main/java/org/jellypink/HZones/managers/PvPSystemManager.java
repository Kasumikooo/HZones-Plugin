package org.jellypink.HZones.managers;

import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.UUID;

public class PvPSystemManager {

    private final HashSet<UUID> flaggedPlayers = new HashSet<>();

    public boolean hasPvPEnabled(Player player) {
        return flaggedPlayers.contains(player.getUniqueId());
    }

    public void setPvP(Player player, boolean enabled) {
        if (enabled) {
            flaggedPlayers.add(player.getUniqueId());
        } else {
            flaggedPlayers.remove(player.getUniqueId());
        }
    }

    public void removePlayerOnQuit(Player player) {
        flaggedPlayers.remove(player.getUniqueId());
    }
}