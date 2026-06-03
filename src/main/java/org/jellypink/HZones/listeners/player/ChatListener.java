package org.jellypink.HZones.listeners.player;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);

        event.setMessage(message);

        final String[] notifyMessage = event.getMessage().split(" ");
        final Set<Player> notify = new HashSet<>();

        for (int i = 0; i < notifyMessage.length; i++) {
            final String currentWord = notifyMessage[i];
            final Player player;

            if (currentWord.matches("^\\w{3,16}(\\p{Punct})*$")
                    && (player = Bukkit.getPlayerExact(currentWord.replaceAll("\\p{Punct}", ""))) != null) {
                notifyMessage[i] = "§a@" + player.getName() + "§r" + currentWord.substring(player.getName().length());
                notify.add(player);
            }
        }

        for (Player notified : notify)
            notified.playSound(notified.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        event.setMessage(String.join(" ", notifyMessage));
    }
}
