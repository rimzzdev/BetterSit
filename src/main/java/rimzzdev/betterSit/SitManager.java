package rimzzdev.betterSit;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import rimzzdev.betterSit.config.BetterSitConfig;
import rimzzdev.betterSit.utils.BlockCategory;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SitManager {

    private final Map<UUID, ArmorStand> sittingPlayers = new Object2ObjectOpenHashMap<>();
    private final Map<UUID, ArmorStand> layingPlayers = new Object2ObjectOpenHashMap<>();
    private final Map<UUID, Long> cooldowns = new Object2ObjectOpenHashMap<>();
    private final Map<UUID, Set<BlockCategory>> disabledCategories = new Object2ObjectOpenHashMap<>();
    private final BetterSitConfig config;

    public SitManager(BetterSitConfig config) {
        this.config = config;
    }

    public boolean sit(Player player) {
        if (isSitting(player)) return false;
        if (isLaying(player)) unlay(player);
        return sit(player, player.getLocation(), false);
    }

    public boolean sit(Player player, Location location) {
        return sit(player, location, false);
    }

    public boolean lay(Player player) {
        if (isLaying(player)) return false;
        if (isSitting(player)) unsit(player);
        return lay(player, player.getLocation());
    }

    public boolean lay(Player player, Location location) {
        return sit(player, location, true);
    }

    private boolean sit(Player player, Location location, boolean isLay) {
        if (isSitting(player) || isLaying(player)) return false;

        Location loc = location.clone();
        if (config.isCenterOnBlock()) {
            loc.setX(loc.getBlockX() + 0.5);
            loc.setZ(loc.getBlockZ() + 0.5);
        }

        double offset = isLay ? config.getLayHeightOffset() : config.getSitHeightOffset();
        loc.setY(loc.getY() + offset);

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
        if (isLay) {
            layingPlayers.put(player.getUniqueId(), stand);
        } else {
            sittingPlayers.put(player.getUniqueId(), stand);
        }
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

    public boolean unlay(Player player) {
        ArmorStand stand = layingPlayers.remove(player.getUniqueId());
        if (stand != null && !stand.isDead()) {
            stand.removePassenger(player);
            stand.remove();
            return true;
        }
        return false;
    }

    public void standUp(Player player) {
        if (!unsit(player)) {
            unlay(player);
        }
    }

    public boolean isSitting(Player player) {
        return sittingPlayers.containsKey(player.getUniqueId());
    }

    public boolean isLaying(Player player) {
        return layingPlayers.containsKey(player.getUniqueId());
    }

    public boolean isAny(Player player) {
        return isSitting(player) || isLaying(player);
    }

    public long getCooldownRemaining(Player player) {
        int cooldownSeconds = config.getSitCooldown();
        if (cooldownSeconds <= 0) return 0;
        long lastUsed = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        long now = System.currentTimeMillis();
        long timeLeft = (lastUsed + cooldownSeconds * 1000L) - now;
        return Math.max(timeLeft, 0);
    }

    public void updateCooldown(Player player) {
        if (config.getSitCooldown() > 0) {
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    public boolean isCategoryAllowed(Player player, BlockCategory category) {
        Set<BlockCategory> disabled = disabledCategories.get(player.getUniqueId());
        return disabled == null || !disabled.contains(category);
    }

    public void toggleCategory(Player player, BlockCategory category) {
        disabledCategories.compute(player.getUniqueId(), (uuid, set) -> {
            if (set == null) set = new ObjectOpenHashSet<>();
            if (set.contains(category)) set.remove(category);
            else set.add(category);
            return set.isEmpty() ? null : set;
        });
    }

    public void unsitAll() {
        new Object2ObjectOpenHashMap<>(sittingPlayers).forEach((uuid, stand) -> {
            if (!stand.getPassengers().isEmpty() && stand.getPassengers().getFirst() instanceof Player player) {
                unsit(player);
            } else {
                stand.remove();
            }
        });
        new Object2ObjectOpenHashMap<>(layingPlayers).forEach((uuid, stand) -> {
            if (!stand.getPassengers().isEmpty() && stand.getPassengers().getFirst() instanceof Player player) {
                unlay(player);
            } else {
                stand.remove();
            }
        });
        sittingPlayers.clear();
        layingPlayers.clear();
    }

    @SuppressWarnings("unused")
    public BetterSitConfig getConfig() {
        return config;
    }

    public boolean sitAtCampfire(Player player, Block campfire) {
        if (isAny(player)) return false;

        Location sitLoc = findSitLocation(campfire, player);
        if (sitLoc == null) return false;

        if (sit(player, sitLoc)) {
            return true;
        }
        return false;
    }

    private Location findSitLocation(Block campfire, Player player) {
        Location center = campfire.getLocation().clone().add(0.5, 0, 0.5);
        int[][] offsets = {
                {1, 0}, {1, 1}, {0, 1}, {-1, 1},
                {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
        };
        double playerX = player.getLocation().getX();
        double playerZ = player.getLocation().getZ();
        Location bestLoc = null;
        double bestDistance = Double.MAX_VALUE;

        for (int[] offset : offsets) {
            Location loc = center.clone().add(offset[0], 0, offset[1]);
            Block block = loc.getBlock();
            Block above = block.getRelative(0, 1, 0);

            if (block.getType().isAir() && above.getType().isAir()) {
                boolean occupied = loc.getWorld().getNearbyEntities(loc, 0.5, 0.5, 0.5).stream()
                        .anyMatch(e -> e instanceof Player && !e.equals(player));
                if (!occupied) {
                    double dist = Math.hypot(loc.getX() - playerX, loc.getZ() - playerZ);
                    if (dist < bestDistance) {
                        bestDistance = dist;
                        bestLoc = loc.clone();
                    }
                }
            }
        }
        return bestLoc;
    }
}