package me.monkeee.simpleBanHammer.events;

import de.tr7zw.changeme.nbtapi.NBT;
import me.monkeee.simpleBanHammer.SimpleBanHammer;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerHitEvent implements Listener {
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity target = e.getEntity();
        FileConfiguration config = SimpleBanHammer.getinstance().getConfig();
        String banCommand = config.getString("ban-command");
        assert banCommand != null;

        if (damager instanceof Player admin && target instanceof Player victim) {

            if (admin.hasPermission("sbh.usehammer") && !victim.isOp() && !victim.hasPermission("sbh.exampt")) {
                ItemStack weapon = admin.getInventory().getItemInMainHand();
                if (weapon.getType().equals(Material.valueOf("AIR"))) return;
                if (Objects.requireNonNull(weapon.getItemMeta()).getItemName().equals("sbh_hammer")) {
                String reason = NBT.get(weapon, nbt -> {
                    return nbt.getString("Reason");
                });
                banCommand = banCommand.replace("%player%", victim.getName());
                banCommand = banCommand.replace("%reason%", reason);
                    victim.getWorld().strikeLightning(victim.getLocation());
                    if (config.isSet("enable-console-sender") && Objects.requireNonNull(config.getString("enable-console-sender")).equalsIgnoreCase("true")) {
                        banCommand = banCommand + " | by " + admin.getName();
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), banCommand);
                    }
                    else Bukkit.dispatchCommand(admin, banCommand);
                    if (!victim.isOnline()) {
                        admin.sendMessage(ChatColor.GREEN + "Player " + ChatColor.RESET + victim.getName() + ChatColor.GREEN + " has been banned with Reason: " + ChatColor.WHITE + reason);
                        String adminLog = admin.getName() + " (" + admin.getUniqueId() + ")";
                        String playerLog = victim.getName() + " (" + victim.getUniqueId() + ")";
                        SimpleBanHammer.getinstance().getLogger().warning("Admin " + adminLog + " has used SimpleBanHammer on player " + playerLog);
                        if (config.getBoolean("enable-broadcast")) {
                            for (Player users : Bukkit.getOnlinePlayers()) {
                                String message = config.getString("broadcast-message");
                                assert message != null;
                                message = message.replace("%player%", getPlayerPrefix(victim));
                                message = message.replace("%prefix_player%", getPlayerPrefix(victim));
                                message = message.replace("%admin%", getPlayerPrefix(admin));
                                message = message.replace("%prefix_admin%", getPlayerPrefix(admin));
                                message = message.replace("%reason%", reason);
                                message = ChatColor.translateAlternateColorCodes('&', message);
                                users.sendMessage(message);
                            }
                        }
                    } else admin.sendMessage(ChatColor.RED + "The ban failed. Please try again later.");
                }
            } else admin.sendMessage(ChatColor.RED + "You don't have the right permissions or the player is Operator!");
        }
    }

    private static String getPlayerPrefix(Player player) {
        if (SimpleBanHammer.getLuckPerms()) {
            User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player);
            String prefix = user.getCachedData().getMetaData().getPrefix();

            if (prefix != null) {
                return prefix + player.getName();
            }
        }
        return player.getName();
    }
}
