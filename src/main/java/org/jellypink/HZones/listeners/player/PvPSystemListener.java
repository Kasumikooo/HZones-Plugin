package org.jellypink.HZones.listeners.player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jellypink.HZones.Main;
import org.jellypink.HZones.managers.PvPSystemManager;
import org.jellypink.HZones.models.ZoneFlagType;
import org.jellypink.HZones.utils.MessageUtils;

public class PvPSystemListener implements Listener {

    private final Main plugin;
    private final PvPSystemManager pvpSystem;

    public PvPSystemListener(Main plugin, PvPSystemManager pvpSystem) {
        this.plugin = plugin;
        this.pvpSystem = pvpSystem;
    }

    public ZoneFlagType getPlayerZone(Player player) {
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
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        ZoneFlagType attackerZone = getPlayerZone(attacker);
        ZoneFlagType victimZone = getPlayerZone(victim);

        if (attackerZone == ZoneFlagType.GREEN || attackerZone == ZoneFlagType.CITY ||
                victimZone == ZoneFlagType.GREEN || victimZone == ZoneFlagType.CITY) {
            event.setCancelled(true);
            attacker.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + plugin.getMainConfigManager().getFight_SafeZone()));
            return;
        }

        if (attackerZone == ZoneFlagType.RED || attackerZone == ZoneFlagType.BLACK) {
            return;
        }

        if (attackerZone == ZoneFlagType.YELLOW && victimZone == ZoneFlagType.YELLOW) {
            if (!pvpSystem.hasPvPEnabled(attacker)) {
                event.setCancelled(true);
                attacker.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + plugin.getMainConfigManager().getPvP_NotEnable()));
            } else if (!pvpSystem.hasPvPEnabled(victim)) {
                event.setCancelled(true);
                attacker.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + plugin.getMainConfigManager().getPvP_EnemyNotEnable()));
            }
        }
    }

    public void updatePlayerNameColor(Player player) {
        String green = ChatColor.GREEN + "● " + ChatColor.WHITE + player.getName();
        String red = ChatColor.RED + "● " + ChatColor.WHITE + player.getName();

        if (pvpSystem.hasPvPEnabled(player)) {
            player.setDisplayName(red);
            player.setPlayerListName(red);
            player.setCustomName(red);
            player.setCustomNameVisible(true);
        } else {
            player.setDisplayName(green);
            player.setPlayerListName(green);
            player.setCustomName(green);
            player.setCustomNameVisible(true);
        }
    }

    private final java.util.HashMap<java.util.UUID, ZoneFlagType> lastKnownZone = new java.util.HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
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

        if (pvpSystem.hasPvPEnabled(player) &&
                (previousZone == ZoneFlagType.RED || previousZone == ZoneFlagType.BLACK) &&
                currentZone == ZoneFlagType.YELLOW &&
                pvpSystem.isInCombat(player)) {

            pvpSystem.setPvP(player, true);
            updatePlayerNameColor(player);

            return;
        }

        handleAutomaticPvPStatus(player, currentZone, previousZone);

    }

    private void handleAutomaticPvPStatus(Player player, ZoneFlagType currentZone, ZoneFlagType previousZone) {

        if (currentZone == ZoneFlagType.RED || currentZone == ZoneFlagType.BLACK) {
            if (!pvpSystem.hasPvPEnabled(player)) {
                pvpSystem.setPvP(player, true);
                player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + plugin.getMainConfigManager().getPvPStatus_ForceEnable()));
            }
        }

        else if (currentZone == ZoneFlagType.GREEN || currentZone == ZoneFlagType.CITY) {
            if (pvpSystem.hasPvPEnabled(player)) {
                pvpSystem.setPvP(player, false);
                player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + plugin.getMainConfigManager().getPvPStatus_ForceDisable()));
            }
        }

        else if (currentZone == ZoneFlagType.YELLOW) {
            if (previousZone == ZoneFlagType.RED || previousZone == ZoneFlagType.BLACK) {
                if (pvpSystem.hasPvPEnabled(player)) {
                    pvpSystem.setPvP(player, false);
                    player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + plugin.getMainConfigManager().getPvPStatus_Neutral()));
                }
            }
        }

        updatePlayerNameColor(player);
    }

    // CombatLog


    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        if (!pvpSystem.hasPvPEnabled(attacker)) return;
        if (!pvpSystem.hasPvPEnabled(victim)) return;

        pvpSystem.tagPlayer(attacker);
        pvpSystem.tagPlayer(victim);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        pvpSystem.removeCombatTag(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (pvpSystem.hasPvPEnabled(player) && pvpSystem.isInCombat(player)) {

            player.setHealth(0);
            Player killer = player.getKiller();
            if (killer != null && killer.isOnline()) {

                player.getWorld().strikeLightningEffect(player.getLocation());
                killer.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

                killer.getPlayer().sendMessage(MessageUtils.getColoredMessage(plugin.getMainConfigManager().getCombatLog_PlayerDisconnect()
                        .replace("%player_name%", player.getName())));
            }
            plugin.getServer().broadcastMessage(MessageUtils.getColoredMessage("&c[Server] &f¡The player &b" +
                    player.getName() + " &fhas disconnect in combat!"));
            if(plugin.getMainConfigManager().getCombatLog_PlayerDisconnect_GlobalEnabled()){
                String playerdisconnectglobal = MessageUtils.getColoredMessage(
                        plugin.getMainConfigManager().getCombatLog_PlayerDisconnect_GlobalMessage()
                                .replace("%player_name%", player.getName()));
            }
        }

        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
        player.setCustomName(null);

        pvpSystem.removePlayerOnQuit(player);
        lastKnownZone.remove(player.getUniqueId());
    }


}