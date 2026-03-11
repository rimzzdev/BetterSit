package rimzzdev.betterSit.utils;

import com.eternalcode.gitcheck.GitCheck;
import com.eternalcode.gitcheck.GitCheckResult;
import com.eternalcode.gitcheck.git.GitRepository;
import com.eternalcode.gitcheck.git.GitTag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final GitCheck gitCheck = new GitCheck();
    private final GitRepository repository;

    public UpdateChecker(JavaPlugin plugin, String owner, String repoName) {
        this.plugin = plugin;
        this.repository = GitRepository.of(owner, repoName);
    }

    public void check() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String currentVersion = plugin.getDescription().getVersion();
                GitTag currentTag = GitTag.of("v" + currentVersion);

                GitCheckResult result = gitCheck.checkRelease(repository, currentTag);

                if (!result.isUpToDate()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            notifyAdmins(result);
                        }
                    }.runTask(plugin);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Update check failed: " + e.getMessage());
            }
        });
    }

    private void notifyAdmins(GitCheckResult result) {
        plugin.getLogger().warning("=========================================");
        plugin.getLogger().warning("New version available: " + result.getLatestRelease().getTag().getTag());
        plugin.getLogger().warning("Current version: " + plugin.getDescription().getVersion());
        plugin.getLogger().warning("Release page: " + result.getLatestRelease().getPageUrl());
        plugin.getLogger().warning("=========================================");

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("betterSit.admin")) {
                player.sendMessage("§8[§aBetterSit§8] §eNew version §a" +
                        result.getLatestRelease().getTag().getTag() + " §eavailable!");
                player.sendMessage("§8[§aBetterSit§8] §7" + result.getLatestRelease().getPageUrl());
            }
        }
    }
}