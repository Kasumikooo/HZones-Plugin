package org.jellypink.HZones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jellypink.HZones.Main;
import org.jellypink.HZones.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabExecutor {

    private final Main plugin;
    private final ZoneCommand zoneCommand;
    private ReloadConfigCommand reloadConfigCommand;

    public MainCommand(Main plugin) {
        this.plugin = plugin;
        this.zoneCommand = new ZoneCommand(plugin);
    }

    // AutoCompleter de los commandos

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        final List<String> validArguments = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("zone"), validArguments);
            return validArguments;

        }

        String rama = args[0].toLowerCase();
        switch (rama) {
            case "zone", "zones", "z":
                return ZoneCommand.onTabComplete(args);
            case "reload":
                return reloadConfigCommand.onTabComplete(args);
        }

        return List.of();
    }

    // ejection del comando

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.ConsoleMessage());
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendGeneralHelp(player);
            return true;
        }

        String rama = args[0].toLowerCase();

        switch (rama) {
            case "zone","z","zones":
                zoneCommand.execute(player, args);
                break;
            case "reload":
                plugin.reloadConfig();
                break;
            default:
                player.sendMessage(MessageUtils.getColoredMessage("&cSubcommand not found. Use /hzones for help."));
                break;
        }

        return true;
    }

    private void sendGeneralHelp(Player player) {
        player.sendMessage(MessageUtils.getColoredMessage("&6====== &bHZones Help &6======"));
        player.sendMessage(MessageUtils.getColoredMessage("&a/hzones zone <subcommand> &7- Gestion of zones"));
        player.sendMessage(MessageUtils.getColoredMessage("&a/pvp &7- PvP enable/disable"));
    }

}