package rimzzdev.betterSit.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

public class ModrinthUpdateChecker {
    private static final String MODRINTH_API = "https://api.modrinth.com/v2/project/bettersit/version";
    private static final String MODRINTH_URL = "https://modrinth.com/plugin/bettersit/version/";

    public static String getLatestVersion() {
        try {
            URI uri = new URI(MODRINTH_API);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("User-Agent", "BetterSit-UpdateChecker");

            if (conn.getResponseCode() != 200) return null;

            try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
                JsonArray versions = JsonParser.parseReader(reader).getAsJsonArray();
                if (versions.isEmpty()) return null;
                JsonObject latest = versions.get(0).getAsJsonObject();
                return latest.get("version_number").getAsString();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void checkForPlayer(Player player, JavaPlugin plugin) {
        if (!player.hasPermission("betterSit.admin")) return;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            String latest = getLatestVersion();
            if (latest == null) return;

            String current = plugin.getPluginMeta().getVersion();
            if (current.equalsIgnoreCase(latest)) return;

            Component message = Component.text()
                    .append(Component.text("[BetterSit] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                    .append(Component.text("New version available: ", NamedTextColor.GREEN))
                    .append(Component.text("v" + latest, NamedTextColor.YELLOW))
                    .append(Component.text(". ", NamedTextColor.GRAY))
                    .append(Component.text("Click here", NamedTextColor.AQUA, TextDecoration.UNDERLINED)
                            .clickEvent(ClickEvent.openUrl(MODRINTH_URL + latest)))
                    .append(Component.text(" to download.", NamedTextColor.GRAY))
                    .build();

            plugin.getServer().getScheduler().runTask(plugin, () -> player.sendMessage(message));
        });
    }
}