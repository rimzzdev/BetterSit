package rimzzdev.betterSit.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterSitConfig {

    private final JavaPlugin plugin;
    private boolean centerOnBlock;
    private double sitHeightOffset;
    private double layHeightOffset;
    private int sitCooldown;
    private String language;
    private boolean campfireEnabled;

    public BetterSitConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        reload();
    }

    public void reload() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        centerOnBlock = config.getBoolean("center-on-block", true);
        sitHeightOffset = config.getDouble("sit-height-offset", -0.2);
        layHeightOffset = config.getDouble("lay-height-offset", -0.8);
        sitCooldown = config.getInt("sit-cooldown", 0);
        language = config.getString("language", "en");
        campfireEnabled = config.getBoolean("campfire-enabled", true);
    }

    public boolean isCenterOnBlock() { return centerOnBlock; }
    public double getSitHeightOffset() { return sitHeightOffset; }
    public double getLayHeightOffset() { return layHeightOffset; }
    public int getSitCooldown() { return sitCooldown; }
    @SuppressWarnings("unused") public String getLanguage() { return language; }
    public boolean isCampfireEnabled() { return campfireEnabled; }
}