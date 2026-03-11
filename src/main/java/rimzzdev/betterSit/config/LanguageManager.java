package rimzzdev.betterSit.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private final JavaPlugin plugin;
    private final Map<String, YamlConfiguration> languages = new HashMap<>();
    private String defaultLanguage;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public LanguageManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadLanguages();
    }

    private void loadLanguages() {
        File langFolder = new File(plugin.getDataFolder(), "languages");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
            plugin.saveResource("languages/en.yml", false);
            plugin.saveResource("languages/ru.yml", false);
        }

        File[] files = langFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                String langCode = file.getName().replace(".yml", "");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                languages.put(langCode, config);
            }
        }

        defaultLanguage = plugin.getConfig().getString("language", "en");
        if (!languages.containsKey(defaultLanguage)) {
            plugin.getLogger().warning("Language '" + defaultLanguage + "' not found, using en");
            defaultLanguage = "en";
        }
    }

    public Component getMessage(String key, Object... placeholders) {
        YamlConfiguration lang = languages.get(defaultLanguage);
        if (lang == null) {
            return Component.text("Missing language file for " + defaultLanguage);
        }

        String message = lang.getString(key);
        if (message == null) {
            plugin.getLogger().warning("Missing message key: " + key + " in language " + defaultLanguage);
            return Component.text("Missing message: " + key);
        }

        // Замена плейсхолдеров {0}, {1}...
        for (int i = 0; i < placeholders.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(placeholders[i]));
        }

        Component component;
        if (message.contains("&") && !message.contains("<")) {
            // Fallback
            component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
        } else {
            component = miniMessage.deserialize(message);
        }

        return component;
    }

    public Component getPrefixedMessage(String key, Object... placeholders) {
        Component prefix = getMessage("prefix");
        Component message = getMessage(key, placeholders);
        return prefix.append(message);
    }

    public void reload() {
        languages.clear();
        loadLanguages();
    }
}