package rimzzdev.betterSit.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import rimzzdev.betterSit.SitManager;
import rimzzdev.betterSit.config.LanguageManager;
import rimzzdev.betterSit.utils.BlockCategory;
import rimzzdev.betterSit.utils.ModrinthUpdateChecker;

public class PlayerListener implements Listener {

    private final SitManager sitManager;
    private final LanguageManager lang;
    private final JavaPlugin plugin;

    public PlayerListener(SitManager sitManager, LanguageManager lang, JavaPlugin plugin) {
        this.sitManager = sitManager;
        this.lang = lang;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (event.isSneaking() && sitManager.isAny(player)) {
            sitManager.standUp(player);
            Component msg = lang.getPrefixedMessage("stood-up");
            if (msg != Component.empty()) player.sendMessage(msg);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        sitManager.standUp(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Используем правильное имя метода – checkForPlayer
        ModrinthUpdateChecker.checkForPlayer(event.getPlayer(), plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        Player player = event.getPlayer();
        if (sitManager.isAny(player)) return;

        Material type = block.getType();
        BlockCategory category = null;

        if (type.name().endsWith("_STAIRS")) {
            category = BlockCategory.STAIRS;
        } else if (type.name().endsWith("_CARPET")) {
            category = BlockCategory.CARPET;
        } else if (type.name().endsWith("_SLAB") && !type.name().contains("PRESSURE_PLATE")) {
            category = BlockCategory.SLAB;
        }

        if (category == null) return;
        if (!sitManager.isCategoryAllowed(player, category)) return;

        long remaining = sitManager.getCooldownRemaining(player);
        if (remaining > 0) {
            Component msg = lang.getPrefixedMessage("cooldown", (remaining / 1000 + 1));
            if (msg != Component.empty()) player.sendMessage(msg);
            return;
        }

        event.setCancelled(true);
        Location sitLoc = block.getLocation().clone().add(0.5, 0, 0.5);

        if (sitManager.sit(player, sitLoc)) {
            sitManager.updateCooldown(player);
            Component msg = lang.getPrefixedMessage("now-sitting");
            if (msg != Component.empty()) player.sendMessage(msg);
        } else {
            Component msg = lang.getPrefixedMessage("sit-failed");
            if (msg != Component.empty()) player.sendMessage(msg);
        }
    }
}