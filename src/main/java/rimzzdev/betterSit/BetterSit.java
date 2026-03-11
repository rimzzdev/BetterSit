package rimzzdev.betterSit;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import rimzzdev.betterSit.commands.ReloadCommand;
import rimzzdev.betterSit.commands.SitCommand;
import rimzzdev.betterSit.config.BetterSitConfig;
import rimzzdev.betterSit.config.LanguageManager;
import rimzzdev.betterSit.listeners.PlayerListener;
import rimzzdev.betterSit.utils.UpdateChecker;

public final class BetterSit extends JavaPlugin {

    private static BetterSit instance;
    private SitManager sitManager;
    private BetterSitConfig pluginConfig;
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        instance = this;

        // Конфигурация
        saveDefaultConfig();
        pluginConfig = new BetterSitConfig(this);

        // Языковой менеджер
        languageManager = new LanguageManager(this);

        // Менеджер сидения
        sitManager = new SitManager(pluginConfig);

        // ACF Command Manager
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new SitCommand(sitManager, languageManager));
        commandManager.registerCommand(new ReloadCommand(this));

        // Слушатель событий
        getServer().getPluginManager().registerEvents(new PlayerListener(sitManager, languageManager), this);

        // Проверка обновлений
        new UpdateChecker(this, "raidenshik", "BetterSit").check();

        getLogger().info("BetterSit enabled.");
    }

    @Override
    public void onDisable() {
        if (sitManager != null) {
            sitManager.unsitAll();
        }
        getLogger().info("BetterSit disabled.");
    }

    public static BetterSit getInstance() {
        return instance;
    }

    public BetterSitConfig getPluginConfig() {
        return pluginConfig;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public SitManager getSitManager() {
        return sitManager;
    }
}