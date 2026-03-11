package rimzzdev.betterSit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import rimzzdev.betterSit.SitManager;
import rimzzdev.betterSit.config.LanguageManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CommandAlias("sit")
@CommandPermission("betterSit.sit")
@Description("Sit down on the spot")
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
        int cooldownSeconds = sitManager.getConfig().getSitCooldown();
        if (cooldownSeconds > 0) {
            long lastUsed = cooldowns.getOrDefault(player.getUniqueId(), 0L);
            long now = System.currentTimeMillis();
            long timeLeft = (lastUsed + cooldownSeconds * 1000L) - now;
            if (timeLeft > 0) {
                player.sendMessage(lang.getPrefixedMessage("cooldown", (timeLeft / 1000 + 1)));
                return;
            }
        }

        if (sitManager.isSitting(player)) {
            player.sendMessage(lang.getPrefixedMessage("already-sitting"));
            return;
        }

        if (sitManager.sit(player)) {
            if (cooldownSeconds > 0) {
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
            }
            player.sendMessage(lang.getPrefixedMessage("now-sitting"));
        } else {
            player.sendMessage(lang.getPrefixedMessage("sit-failed"));
        }
    }
}