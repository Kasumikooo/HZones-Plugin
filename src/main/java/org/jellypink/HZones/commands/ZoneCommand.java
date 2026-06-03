package org.jellypink.HZones.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import org.jellypink.HZones.Main;
import org.jellypink.HZones.config.MainConfigManager;
import org.jellypink.HZones.utils.MessageUtils;
import org.jellypink.HZones.models.ZoneType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ZoneCommand {

    public static List<String> onTabComplete(String[] args) {

        final List<String> validArguments = new ArrayList<>();

        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], List.of("create", "delete", "list"), validArguments);
            return validArguments;

        }
        if (args[1].equalsIgnoreCase("create")) {
            if (args.length == 4) {
                String currentArg = (args.length == 4) ? args[3] : "";

                StringUtil.copyPartialMatches(currentArg, List.of("green", "yellow", "red", "black", "city"), validArguments);
                return validArguments;
            }
        }
        return List.of();
    }

    private final Main plugin;

    public ZoneCommand(Main plugin) {
        this.plugin = plugin;
    }

    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            SendHelp(player);
            return;
        }

        String SubCommand = args[1].toLowerCase();

        switch(SubCommand) {
            case "create":
                // comandos
                handleCreate(player, args);
                break;
            case "delete":
                // comandos
                handleDelete(player, args);
                break;
            case "list":
                // comandos
                handleList(player, args);
                break;
            default:
                player.sendMessage(MessageUtils.getColoredMessage("&cThis command doesn't exist. try with: create, delete, flag or list."));
                break;
        }

        return;
    }

    private void handleCreate(Player player, String[] args) {
        if (!player.hasPermission("hyterMMO.admin.zcreate")) {
            player.sendMessage(MessageUtils.getColoredMessage("&cYou do not have permission to use this command!"));
            return;
        }

        if (args.length != 4) {
            player.sendMessage(MessageUtils.getColoredMessage("Usage: /hmmo z create <region_id> <type>"));
            return;
        }

        String regionId = args[2];
        String zoneTypeInput = args[3];

        ZoneType zoneType = ZoneType.fromString(zoneTypeInput);
        if (zoneType == null) {
            player.sendMessage(MessageUtils.getColoredMessage("&cInvalid or unsupported zone type. (green, yellow, red, black, city)"));
            return;
        }

        Region selection;
        World weWorld = BukkitAdapter.adapt(player.getWorld());
        try {
            LocalSession session = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player));
            selection = session.getSelection(weWorld);
        } catch (IncompleteRegionException e) {
            player.sendMessage(MessageUtils.getColoredMessage("&cYour WorldEdit Selection is Incomplete. Please select two points."));
            return;
        }

        if (selection == null) {
            player.sendMessage(MessageUtils.getColoredMessage("&cInvalid or unsupported WorldEdit Selection type. Please make a cuboid (//pos1, //pos2) selection."));
            return;
        }

        BlockVector3 min = selection.getMinimumPoint();
        BlockVector3 max = selection.getMaximumPoint();

        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(weWorld);

        if(regionManager == null) {
            player.sendMessage(MessageUtils.getColoredMessage("&cInternal error: Could not manage regions for your current world."));
            return;
        }

        if (regionManager.hasRegion(regionId)) {
            player.sendMessage(MessageUtils.getColoredMessage("&cRegion with that name already exists."));
            return;
        }

        ProtectedRegion region = new ProtectedCuboidRegion(regionId, min, max);

        // call ZoneMethod
        ZoneMethod(region, zoneType);

        regionManager.addRegion(region);
        try {
            regionManager.save();
            player.sendMessage(MessageUtils.getColoredMessage("&aSuccessfully created region " + regionId + " as " + zoneType.getDisplayName() + "&a!"));
        } catch (Exception ex) {
            ex.printStackTrace();
            player.sendMessage(MessageUtils.getColoredMessage("&cAn error occurred while saving the region."));
        }

    }

    // ZoneMethod

    private void ZoneMethod(ProtectedRegion region, ZoneType type) {
        MainConfigManager mainConfigManager = plugin.getMainConfigManager();

        if(mainConfigManager.isZone_Welcome_MessageEnabled()){
            String greetingsMessage = MessageUtils.getColoredMessage(
                    plugin.getMainConfigManager().getZone_Welcome_Message().replace("%zone_name%", region.getId()).replace("%zone_type%", type.getDisplayName()));
            region.setFlag(Flags.GREET_MESSAGE, greetingsMessage);
        }

        switch (type) {
            case GREEN:
                region.setFlag(Main.GREEN_ZONE, StateFlag.State.ALLOW);
                region.setFlag(Flags.SNOWMAN_TRAILS, StateFlag.State.ALLOW);
                region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
                region.setFlag(Flags.PVP, StateFlag.State.ALLOW);
                region.setFlag(Flags.BLOCK_PLACE, StateFlag.State.DENY);
                break;

            case YELLOW:
                region.setFlag(Main.YELLOW_ZONE, StateFlag.State.ALLOW);
                region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
                region.setFlag(Flags.PVP, StateFlag.State.ALLOW);
                region.setFlag(Flags.BLOCK_PLACE, StateFlag.State.DENY);
                break;

            case RED:
                region.setFlag(Main.RED_ZONE, StateFlag.State.ALLOW);
                region.setFlag(Flags.SNOWMAN_TRAILS, StateFlag.State.ALLOW);
                region.setFlag(Flags.PVP, StateFlag.State.ALLOW);
                region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
                region.setFlag(Flags.BLOCK_PLACE, StateFlag.State.DENY);
                break;

            case BLACK:
                region.setFlag(Main.BLACK_ZONE, StateFlag.State.ALLOW);
                region.setFlag(Flags.SNOWMAN_TRAILS, StateFlag.State.ALLOW);
                region.setFlag(Flags.PVP, StateFlag.State.ALLOW);
                region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
                region.setFlag(Flags.BLOCK_PLACE, StateFlag.State.DENY);
                break;
            case CITY:
                region.setFlag(Main.CITY_ZONE, StateFlag.State.ALLOW);
                region.setFlag(Flags.SNOWMAN_TRAILS, StateFlag.State.ALLOW);
                region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
                region.setFlag(Flags.PVP, StateFlag.State.ALLOW);
                region.setFlag(Flags.BLOCK_PLACE, StateFlag.State.DENY);
                break;
        }
    }

    private void handleList(Player player, String[] args) {
        if (!player.hasPermission("hyterMMO.admin.list")) {
            player.sendMessage(MessageUtils.getColoredMessage("&cYou do not have permission to use this command!"));
            return;
        }

        // Region list

        org.bukkit.World world = player.getWorld();
        World weWorld = BukkitAdapter.adapt(world);
        var regionManager =  WorldGuard.getInstance().getPlatform().getRegionContainer().get(weWorld);

        if(regionManager == null) {
            player.sendMessage(MessageUtils.getColoredMessage("&cInternal error: Could not manage regions for your current world."));
            return;
        }

        Map<String, ProtectedRegion> regionsMap = regionManager.getRegions();
        ArrayList<ProtectedRegion> RegionsList = new ArrayList<>();

        for (var region : regionsMap.values()) {
            var flagState = region.getFlag(Flags.SNOWMAN_TRAILS);
            if (flagState == StateFlag.State.ALLOW) {
                RegionsList.add(region);
            }
        }

        if (RegionsList.isEmpty()) {
            player.sendMessage(MessageUtils.getColoredMessage("&cNo regions have found in your current world."));
            return;
        }else {
            player.sendMessage(MessageUtils.getColoredMessage("&b---Zones Regions ---"));
            for (ProtectedRegion region :  RegionsList) {

                BlockVector3 min = region.getMinimumPoint();
                BlockVector3 max = region.getMaximumPoint();

                double centerX = (min.x() + max.x() + 1) / 2.0;
                double centerY = (min.y() + max.y() + 1) / 2.0;
                double centerZ = (min.z() + max.z() + 1) / 2.0;

                String teleportCommand = String.format("/execute in %s run tp %s %.2f %.2f %.2f",
                        world.getKey().toString(),
                        player.getName(),
                        centerX,
                        centerY,
                        centerZ);

                TextComponent message = new TextComponent(MessageUtils.getColoredMessage("&a- ") + region.getId());
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, teleportCommand));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(MessageUtils.getColoredMessage("&7Click to teleport to the center of ") + region.getId())));

                player.spigot().sendMessage(message);
            }
        }
    }

    // Delete command

    private void handleDelete(Player player, String[] args) {
        if (!player.hasPermission("hyterMMO.admin.delete")) {
            player.sendMessage(MessageUtils.getColoredMessage("&cYou do not have permission to use this command."));
            return;
        }

        if (args.length != 2) {
            player.sendMessage(MessageUtils.getColoredMessage("&7Usage: /delete <region_id>"));
            return;
        }

        String regionId = args[1];

        org.bukkit.World world = player.getWorld();
        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(weWorld);

        if (regionManager == null) {
            plugin.getLogger().severe("Could not get RegionManager for world: " + world.getName());
            player.sendMessage(MessageUtils.getColoredMessage("&cInternal error: Could not manage regions for your current world."));
            return;
        }

        ProtectedRegion region = regionManager.getRegion(regionId);
        if (region == null) {
            player.sendMessage(MessageUtils.getColoredMessage("&cA region with the ID '" + regionId + "' does not exist in this world."));
            return;
        }

        // Make sure the region has the flag that we created
        StateFlag.State pvpState = region.getFlag(Flags.SNOWMAN_TRAILS);
        if (pvpState != StateFlag.State.ALLOW) {
            player.sendMessage(MessageUtils.getColoredMessage("&cThe region '" + regionId + "' was not created by this plugin. It will not be deleted."));
            return;
        }

        regionManager.removeRegion(regionId);

        try {
            regionManager.save();
            player.sendMessage(MessageUtils.getColoredMessage("&aSuccessfully deleted PVP region '" + regionId + "'."));
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "An unexpected error occurred while saving regions after deleting " + regionId, e);
            player.sendMessage(MessageUtils.getColoredMessage("&cAn unexpected error occurred while saving the deletion."));
        }
    }

    private void SendHelp(Player player) {
        player.sendMessage(MessageUtils.getColoredMessage("&6====== &bZones Help &6======"));
        player.sendMessage(MessageUtils.getColoredMessage("&a/hmmo z create &7- Gestión de regiones del MMO"));
        player.sendMessage(MessageUtils.getColoredMessage("&a/hmmo z delete &7- Gestión de monstruos personalizados"));
        player.sendMessage(MessageUtils.getColoredMessage("&a/hmmo z list &7- Gestión de minería"));
    }
}
