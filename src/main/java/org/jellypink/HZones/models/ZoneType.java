package org.jellypink.HZones.models;

public enum ZoneType {

    GREEN("&aGreen"),
    YELLOW("&eYellow"),
    RED("&cRed"),
    BLACK("&8Black"),
    CITY("&bCity");

    public final String displayName;

    ZoneType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ZoneType fromString(String text) {
        for (ZoneType type : ZoneType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}
