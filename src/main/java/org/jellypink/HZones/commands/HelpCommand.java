package org.jellypink.HZones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jellypink.HZones.Main;
import org.jellypink.HZones.utils.MessageUtils;

public class HelpCommand implements CommandExecutor {

    private final Main plugin;

    public HelpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.ConsoleMessage());
            return true;
        }

        final Player p = (Player) sender;



        return true;
    }
}
