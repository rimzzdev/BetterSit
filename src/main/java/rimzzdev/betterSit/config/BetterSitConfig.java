package rimzzdev.betterSit.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterSitConfig {

    private final JavaPlugin plugin;
    private boolean centerOnBlock;
    private double sitHeightOffset;
    private int sitCooldown;
    private String language;

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
        sitCooldown = config.getInt("sit-cooldown", 0);
        language = config.getString("language", "en");
    }

    public boolean isCenterOnBlock() {
        return centerOnBlock;
    }

    public double getSitHeightOffset() {
        return sitHeightOffset;
    }

    public int getSitCooldown() {
        return sitCooldown;
    }

    public String getLanguage() {
        return language;
    }
}