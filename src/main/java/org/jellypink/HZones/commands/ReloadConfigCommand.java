package org.jellypink.HZones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jellypink.HZones.Main;
import org.jellypink.HZones.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class ReloadConfigCommand{

    public static List<String> onTabComplete(String[] args) {

        final List<String> validArguments = new ArrayList<>();

        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], List.of("all"), validArguments);
            return validArguments;

        }
        return List.of();
    }

    private final Main plugin;

    public ReloadConfigCommand(Main plugin) {
        this.plugin = plugin;
    }

    public void execute(String[] args, CommandSender sender) {
        if (args.length < 2) {
            sender.sendMessage(MessageUtils.getColoredMessage("&cThis command don't Exist!"));
            return;
        }

        String SubCommand = args[1].toLowerCase();

        switch(SubCommand) {
            case "all":
                ReloadHandle(sender);
                break;
            default:
                sender.sendMessage(MessageUtils.getColoredMessage("&cThis command doesn't exist. try with /hzone reload all"));
                break;
        }
        return;
    }

    public void ReloadHandle(CommandSender sender){
        if (!sender.hasPermission("hzone.reload.all")) {
            sender.sendMessage(MessageUtils.getColoredMessage("&cYou do not have permission to use this command!"));
            return;
        }
        sender.sendMessage(MessageUtils.getColoredMessage("&aReloading config..."));
        plugin.reloadConfig();
        sender.sendMessage(MessageUtils.getColoredMessage("&aConfig Reloaded!"));

    }
}