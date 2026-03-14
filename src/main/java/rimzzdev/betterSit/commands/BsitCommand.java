package rimzzdev.betterSit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rimzzdev.betterSit.BetterSit;
import rimzzdev.betterSit.SitManager;
import rimzzdev.betterSit.config.LanguageManager;
import rimzzdev.betterSit.utils.BlockCategory;

@CommandAlias("bsit|bettersit")
@Description("BetterSit commands")
@SuppressWarnings("unused, deprecation")
public class BsitCommand extends BaseCommand {

    private final BetterSit plugin;
    private final LanguageManager lang;
    private final SitManager sitManager;

    public BsitCommand(BetterSit plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
        this.sitManager = plugin.getSitManager();
    }

    @Default
    @Subcommand("reload")
    @CommandPermission("betterSit.admin")
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
    @CommandPermission("betterSit.admin")
    @Description("Show plugin version")
    public void onVersion(CommandSender sender) {
        String version = plugin.getDescription().getVersion();
        String author = String.join(", ", plugin.getDescription().getAuthors());
        String modrinthUrl = "https://modrinth.com/plugin/bettersit";

        Component message = lang.getMessage("version-info", version, author, modrinthUrl);
        if (message != Component.empty()) {
            sender.sendMessage(message);
        } else {
            sender.sendMessage(Component.text("BetterSit version " + version + " by " + author));
        }
    }

    @Subcommand("toggle")
    @Description("Toggle sitting on blocks by right-click (stairs/slab/carpet/campfire)")
    public void onToggle(Player player, String category) {
        BlockCategory cat;
        try {
            cat = BlockCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(Component.text("Invalid category. Use: stairs, slab, carpet, campfire").color(NamedTextColor.RED));
            return;
        }

        sitManager.toggleCategory(player, cat);
        boolean nowAllowed = sitManager.isCategoryAllowed(player, cat);
        String status = nowAllowed ? "enabled" : "disabled";
        player.sendMessage(Component.text("Sitting on " + category + " is now " + status + ".").color(NamedTextColor.GREEN));
    }
}