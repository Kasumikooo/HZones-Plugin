package org.jellypink.HZones.managers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jellypink.HZones.Main;
import org.jellypink.HZones.config.MainConfigManager;
import org.jellypink.HZones.utils.MessageUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class PvPSystemManager {

    private final HashSet<UUID> flaggedPlayers = new HashSet<>();

    private final Main plugin;
    public PvPSystemManager(Main plugin) {
        this.plugin = plugin;
    }

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

    private final Map<UUID, Long> combatTagged = new HashMap<>();

    public void tagPlayer(Player player) {

        boolean alreadyTagged = isInCombat(player);

        combatTagged.put(
                player.getUniqueId(),
                System.currentTimeMillis()
        );

        if (!alreadyTagged) {
            player.sendMessage(MessageUtils.getColoredMessage(plugin.getMainConfigManager().getCombatLog_InCombat()));
        }
    }

    public boolean isInCombat(Player player) {
        Long lastCombat = combatTagged.get(player.getUniqueId());

        if (lastCombat == null) {
            return false;
        };

        return (System.currentTimeMillis() - lastCombat) < 30000;
    }

    public void removeCombatTag(Player player) {
        combatTagged.remove(player.getUniqueId());
    }

    public long getRemainingCombatTime(Player player) {
        Long lastCombat = combatTagged.get(player.getUniqueId());

        if (lastCombat == null) {
            return 0;
        }

        long remaining = 30000 - (System.currentTimeMillis() - lastCombat);

        return Math.max(0, remaining / 1000);
    }

    public void removePlayerOnQuit(Player player) {
        flaggedPlayers.remove(player.getUniqueId());
    }
}