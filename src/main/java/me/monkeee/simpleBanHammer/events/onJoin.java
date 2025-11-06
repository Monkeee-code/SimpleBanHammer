package me.monkeee.simpleBanHammer.events;

import me.monkeee.simpleBanHammer.SimpleBanHammer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class onJoin implements Listener {

    private static final String currVer = SimpleBanHammer.getinstance().getDescription().getVersion();
    private static final String latestVer = SimpleBanHammer.getLastVer();
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        boolean notifier = SimpleBanHammer.getinstance().getConfig().getBoolean("update-notifier");
        if (notifier) {
            Player player = e.getPlayer();
            if (!player.isOp()) return;

            // Run update check asynchronously
            Bukkit.getScheduler().runTaskAsynchronously(SimpleBanHammer.getinstance(), () -> {
                if (latestVer != null && !Objects.equals(currVer, latestVer)) {
                    Bukkit.getScheduler().runTask(SimpleBanHammer.getinstance(), () -> player.sendMessage(ChatColor.YELLOW + "A new version of SimpleBanHammer is available! " + ChatColor.RED + currVer + ChatColor.WHITE + " -> " + ChatColor.GREEN + latestVer));
                }
            });
        }
    }
}
