package rimzzdev.betterSit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import rimzzdev.betterSit.BetterSit;
import rimzzdev.betterSit.config.LanguageManager;

@CommandAlias("bsit|bettersit")
@CommandPermission("betterSit.admin")
@Description("BetterSit admin commands")
public class BsitCommand extends BaseCommand {

    private final BetterSit plugin;
    private final LanguageManager lang;

    public BsitCommand(BetterSit plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
    }

    @Default
    @Subcommand("reload")
    @Description("Reload configuration and languages")
    public void onReload(CommandSender sender) {
        plugin.reloadConfig();
        plugin.getPluginConfig().reload();
        plugin.getLanguageManager().reload();
        Component msg = lang.getPrefixedMessage("reload-success");
        if (msg != Component.empty()) {
            sender.sendMessage(msg);
        }
    }

    @Subcommand("version")
    @Description("Show plugin version")
    public void onVersion(CommandSender sender) {
        String version = plugin.getDescription().getVersion();
        String author = String.join(", ", plugin.getDescription().getAuthors());
        String githubUrl = "https://github.com/raidenshik/BetterSit";

        Component message = lang.getMessage("version-info", version, author, githubUrl);
        if (message != Component.empty()) {
            sender.sendMessage(message);
        } else {
            // fallback, если сообщение отключено
            sender.sendMessage(Component.text("BetterSit version " + version + " by " + author));
        }
    }
}