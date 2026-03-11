package rimzzdev.betterSit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import rimzzdev.betterSit.SitManager;
import rimzzdev.betterSit.config.LanguageManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CommandAlias("sit")
@CommandPermission("betterSit.sit")
@Description("Sit down or stand up")
public class SitCommand extends BaseCommand {

    private final SitManager sitManager;
    private final LanguageManager lang;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public SitCommand(SitManager sitManager, LanguageManager lang) {
        this.sitManager = sitManager;
        this.lang = lang;
    }

    @Default
    public void onSit(Player player) {
        if (sitManager.isSitting(player)) {
            if (sitManager.unsit(player)) {
                sendIfNotEmpty(player, lang.getPrefixedMessage("stood-up"));
            } else {
                player.sendMessage(Component.text("Could not stand up.").color(NamedTextColor.RED));
            }
            return;
        }

        int cooldownSeconds = sitManager.getConfig().getSitCooldown();
        if (cooldownSeconds > 0) {
            long lastUsed = cooldowns.getOrDefault(player.getUniqueId(), 0L);
            long now = System.currentTimeMillis();
            long timeLeft = (lastUsed + cooldownSeconds * 1000L) - now;
            if (timeLeft > 0) {
                sendIfNotEmpty(player, lang.getPrefixedMessage("cooldown", (timeLeft / 1000 + 1)));
                return;
            }
        }

        if (sitManager.sit(player)) {
            if (cooldownSeconds > 0) {
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
            }
            sendIfNotEmpty(player, lang.getPrefixedMessage("now-sitting"));
        } else {
            sendIfNotEmpty(player, lang.getPrefixedMessage("sit-failed"));
        }
    }

    private void sendIfNotEmpty(Player player, Component message) {
        if (message != Component.empty()) {
            player.sendMessage(message);
        }
    }
}