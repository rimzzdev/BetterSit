package rimzzdev.betterSit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import rimzzdev.betterSit.SitManager;
import rimzzdev.betterSit.config.LanguageManager;

public class PlayerListener implements Listener {

    private final SitManager sitManager;
    private final LanguageManager lang;

    public PlayerListener(SitManager sitManager, LanguageManager lang) {
        this.sitManager = sitManager;
        this.lang = lang;
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (event.isSneaking() && sitManager.isSitting(player)) {
            sitManager.unsit(player);
            player.sendMessage(lang.getPrefixedMessage("stood-up"));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        sitManager.unsit(event.getPlayer());
    }
}