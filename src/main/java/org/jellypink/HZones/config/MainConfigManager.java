package org.jellypink.HZones.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.jellypink.HZones.Main;

public class MainConfigManager {

    private Config configFile;
    private Main plugin;

    /*
        MESSAGES
    --------------------------
    */

    private String PvP_Command_NotYellowZone;
    private String PvP_Enable;
    private String PvP_Disable;

    private String Fight_SafeZone;
    private String PvP_NotEnable;
    private String PvP_EnemyNotEnable;

    private String PvPStatus_ForceEnable;
    private String PvPStatus_ForceDisable;
    private String PvPStatus_Neutral;

    // Zones boolean

    private boolean Zone_Welcome_MessageEnabled;
    private String Zone_Welcome_Message;

    private boolean Green_Zone_DeathEnabled;
    private String Green_Zone_Death;

    private boolean Yellow_Zone_DeathEnabled;
    private String Yellow_Zone_Death;

    private boolean Black_Zone_DeathEnabled;
    private String Black_Zone_Death;

    /*
    --------------------------
    */

    public MainConfigManager(Main plugin){
        this.plugin = plugin;

        configFile = new Config("config.yml", null, plugin, false);
        configFile.registerConfig();
        loadConfig();
    }

    public void loadConfig(){
        FileConfiguration config = configFile.getConfig();

        // PvP

        PvP_Command_NotYellowZone =  config.getString("messages.PvP_Command_NotYellowZone");
        PvP_Enable = config.getString("messages.PvP_Enable");
        PvP_Disable =  config.getString("messages.PvP_Disable");

        Fight_SafeZone = config.getString("messages.Fight_SafeZone");
        PvP_NotEnable = config.getString("messages.PvP_NotEnable");
        PvP_EnemyNotEnable = config.getString("messages.PvP_EnemyNotEnable");

        PvPStatus_ForceEnable = config.getString("messages.PvPStatus_ForceEnable");
        PvPStatus_ForceDisable = config.getString("messages.PvPStatus_ForceDisable");
        PvPStatus_Neutral = config.getString("messages.PvPStatus_Neutral");

        // Zones

        Zone_Welcome_MessageEnabled = config.getBoolean("messages.Zone_Welcome_Message.enabled");
        Zone_Welcome_Message = config.getString("messages.Zone_Welcome_Message.message");

        Green_Zone_DeathEnabled = config.getBoolean("messages.Green_Zone_Death.enabled");
        Green_Zone_Death = config.getString("messages.Green_Zone_Death.message");

        Yellow_Zone_DeathEnabled = config.getBoolean("messages.Yellow_Zone_Death.enabled");
        Yellow_Zone_Death = config.getString("messages.Yellow_Zone_Death.message");

        Black_Zone_DeathEnabled = config.getBoolean("messages.Black_Zone_Death.enabled");
        Black_Zone_Death = config.getString("messages.Black_Zone_Death.message");
    }

    public void ReloadConfig(){
        configFile.reloadConfig();
        loadConfig();
    }

    public String getBlack_Zone_Death() {
        return Black_Zone_Death;
    }

    public String getYellow_Zone_Death() {
        return Yellow_Zone_Death;
    }

    public boolean isBlack_Zone_DeathEnabled() {
        return Black_Zone_DeathEnabled;
    }

    public boolean isYellow_Zone_DeathEnabled() {
        return Yellow_Zone_DeathEnabled;
    }

    public String getGreen_Zone_Death() {
        return Green_Zone_Death;
    }

    public boolean isGreen_Zone_DeathEnabled() {
        return Green_Zone_DeathEnabled;
    }

    public String getZone_Welcome_Message() {
        return Zone_Welcome_Message;
    }

    public boolean isZone_Welcome_MessageEnabled() {
        return Zone_Welcome_MessageEnabled;
    }

    public String getPvPStatus_Neutral() {
        return PvPStatus_Neutral;
    }

    public String getPvPStatus_ForceDisable() {
        return PvPStatus_ForceDisable;
    }

    public String getPvPStatus_ForceEnable() {
        return PvPStatus_ForceEnable;
    }

    public String getPvP_EnemyNotEnable() {
        return PvP_EnemyNotEnable;
    }

    public String getPvP_NotEnable() {
        return PvP_NotEnable;
    }

    public String getFight_SafeZone() {
        return Fight_SafeZone;
    }

    public String getPvP_Disable() {
        return PvP_Disable;
    }

    public String getPvP_Enable() {
        return PvP_Enable;
    }

    public String getPvP_Command_NotYellowZone() {
        return PvP_Command_NotYellowZone;
    }
}
