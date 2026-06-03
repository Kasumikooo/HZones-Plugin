package org.jellypink.HZones.utils;

import org.bukkit.ChatColor;

import static org.jellypink.HZones.Main.prefix;

public class MessageUtils {

    public static String asciiart = """
              ___ _____________                          \s
             /   |   \\____    /____   ____   ____   ______
            /    ~    \\/     //  _ \\ /    \\_/ __ \\ /  ___/
            \\    Y    /     /(  <_> )   |  \\  ___/ \\___ \\\s
             \\___|_  /_______ \\____/|___|  /\\___  >____  >
                   \\/        \\/          \\/     \\/     \\/\s
            """;

    public static String getColoredMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String ConsoleMessage() {
        return MessageUtils.getColoredMessage(prefix+ " &cOnly players can use this command.");
    }

}
