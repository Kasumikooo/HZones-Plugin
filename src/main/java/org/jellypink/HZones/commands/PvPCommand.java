package org.jellypink.HZones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jellypink.HZones.Main;
import org.jellypink.HZones.listeners.player.PlayerDeathListener;
import org.jellypink.HZones.listeners.player.PvPSystemListener;
import org.jellypink.HZones.models.ZoneFlagType;
import org.jellypink.HZones.managers.PvPSystemManager;
import org.jellypink.HZones.utils.MessageUtils;

public class PvPCommand implements CommandExecutor {

    private Main plugin;
    public PvPCommand(Main plugin){
        this.plugin = plugin;
    }

    private PvPSystemManager pvpSystem;
    private PvPSystemListener listener;

    public PvPCommand(Main plugin, PvPSystemManager pvpSystem, PvPSystemListener listener) {
        this.plugin = plugin;
        this.pvpSystem = pvpSystem;
        this.listener = listener;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.ConsoleMessage());
            return true;
        }

        Player player = (Player) sender;
        ZoneFlagType currentZone = listener.getPlayerZone(player);

        if (currentZone != ZoneFlagType.YELLOW) {
            player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + plugin.getMainConfigManager().getPvP_Command_NotYellowZone()));
            return true;
        }

        boolean currentStatus = pvpSystem.hasPvPEnabled(player);
        boolean newStatus = !currentStatus;

        pvpSystem.setPvP(player, newStatus);

        listener.updatePlayerNameColor(player);

        if (newStatus) {
            player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + plugin.getMainConfigManager().getPvP_Enable()));
        } else {
            player.sendMessage(MessageUtils.getColoredMessage(Main.ingameprefix + plugin.getMainConfigManager().getPvP_Disable()));
        }

        return true;
    }
}