package rimzzdev.betterSit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.command.CommandSender;
import rimzzdev.betterSit.BetterSit;

@CommandAlias("bettersit|bsit")
@CommandPermission("betterSit.admin")
@Description("Reload BetterSit configuration and languages")
public class ReloadCommand extends BaseCommand {

    private final BetterSit plugin;

    public ReloadCommand(BetterSit plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onReload(CommandSender sender) {
        plugin.reloadConfig();
        plugin.getPluginConfig().reload();
        plugin.getLanguageManager().reload();
        sender.sendMessage(plugin.getLanguageManager().getPrefixedMessage("reload-success"));
    }
}