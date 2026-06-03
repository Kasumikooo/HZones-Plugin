package org.jellypink.HZones.models;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.jellypink.HZones.Main;

public enum ZoneFlagType {
    GREEN("&aGreen", Main.GREEN_ZONE, 1),
    YELLOW("&eYellow", Main.YELLOW_ZONE, 2),
    RED("&cRed", Main.RED_ZONE, 3),
    BLACK("&8Black", Main.BLACK_ZONE, 4),
    CITY("&bCity", Main.CITY_ZONE, 5);


    private final String displayName;
    private final StateFlag flag;
    private final int value;

    ZoneFlagType(String displayName, StateFlag flag, int value) {
        this.displayName = displayName;
        this.flag = flag;
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public StateFlag getFlag() {
        return flag;
    }

    public int getValue() {
        return value;
    }

    public static ZoneFlagType getActiveZone(ApplicableRegionSet applicableRegions) {
        if (applicableRegions == null || applicableRegions.size() == 0) return null;

        for (ProtectedRegion region : applicableRegions) {

            for (ZoneFlagType zone : ZoneFlagType.values()) {
                if (region.getFlag(zone.getFlag()) == StateFlag.State.ALLOW) {
                    return zone;
                }
            }
        }
        return null;
    }
}
