package rimzzdev.betterSit;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import rimzzdev.betterSit.config.BetterSitConfig;

import java.util.Map;
import java.util.UUID;

public class SitManager {

    private final Map<UUID, ArmorStand> sittingPlayers = new Object2ObjectOpenHashMap<>();
    private final BetterSitConfig config;

    public SitManager(BetterSitConfig config) {
        this.config = config;
    }

    public boolean sit(Player player) {
        if (isSitting(player)) return false;

        Location loc = player.getLocation().clone();

        if (config.isCenterOnBlock()) {
            loc.setX(loc.getBlockX() + 0.5);
            loc.setZ(loc.getBlockZ() + 0.5);
        }

        loc.setY(loc.getY() + config.getSitHeightOffset());

        ArmorStand stand = player.getWorld().spawn(loc, ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setMarker(true);
            armorStand.setGravity(false);
            armorStand.setCanPickupItems(false);
            armorStand.setCustomNameVisible(false);
            armorStand.setSmall(true);
            armorStand.setCollidable(false);
        });

        stand.addPassenger(player);
        sittingPlayers.put(player.getUniqueId(), stand);
        return true;
    }

    public boolean unsit(Player player) {
        ArmorStand stand = sittingPlayers.remove(player.getUniqueId());
        if (stand != null && !stand.isDead()) {
            stand.removePassenger(player);
            stand.remove();
            return true;
        }
        return false;
    }

    public boolean isSitting(Player player) {
        return sittingPlayers.containsKey(player.getUniqueId());
    }

    public void unsitAll() {
        new Object2ObjectOpenHashMap<>(sittingPlayers).forEach((uuid, stand) -> {
            if (!stand.getPassengers().isEmpty() && stand.getPassengers().get(0) instanceof Player player) {
                unsit(player);
            } else {
                stand.remove();
            }
        });
        sittingPlayers.clear();
    }

    public BetterSitConfig getConfig() {
        return config;
    }
}