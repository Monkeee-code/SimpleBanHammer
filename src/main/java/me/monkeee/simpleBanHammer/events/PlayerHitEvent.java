package me.monkeee.simpleBanHammer.events;

import de.tr7zw.changeme.nbtapi.NBT;
import me.monkeee.simpleBanHammer.DiscordWebhook;
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
        // Gets the all the needed necessities
        Entity damager = e.getDamager();
        final FileConfiguration config = SimpleBanHammer.getinstance().getConfig();
        Entity target = e.getEntity();
        String banCommand = config.getString("ban-command");
        assert banCommand != null : "BanCommand is null";

        // Checks if the entities are players
        if (damager instanceof Player admin && target instanceof Player victim) {
            // Checks the player's permissions and op status
            if (admin.hasPermission("sbh.usehammer") && !victim.isOp() && !victim.hasPermission("sbh.exampt")) {
                // Obtains the attacker's weapon in mainhand
                ItemStack weapon = admin.getInventory().getItemInMainHand();
                // Checks if hit by air or hand
                if (weapon.getType().equals(Material.valueOf("AIR"))) return;
                // Checks if the weapon has 'sbh_hammer' custom name and obtains the custom/default reason
                if (Objects.requireNonNull(weapon.getItemMeta()).getItemName().equals("sbh_hammer")) {
                String reason = NBT.get(weapon, nbt -> {
                    return nbt.getString("Reason");
                });
                // Gets the ban command ready, ny replacing the necessary arguments
                banCommand = banCommand.replace("%player%", victim.getName());
                banCommand = banCommand.replace("%reason%", reason);
                // Strikes the player with lightning
                victim.getWorld().strikeLightning(victim.getLocation());
                    // Checks if the command is supposed to be run by the admin ot the console
                    if (config.isSet("enable-console-sender") && config.getBoolean("enable-console-sender")) {
                        banCommand = banCommand + " | by " + admin.getName();
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), banCommand);
                    }
                    else Bukkit.dispatchCommand(admin, banCommand);
                    // Cheks if the player has left the server successfully
                    if (!victim.isOnline()) {
                        // If yes, it sends a few messages in logs, to admin and discord webhook, if set up
                        admin.sendMessage(ChatColor.GREEN + "Player " + ChatColor.RESET + victim.getName() + ChatColor.GREEN + " has been banned with Reason: " + ChatColor.WHITE + reason);
                        String adminLog = admin.getName() + " (" + admin.getUniqueId() + ")";
                        String playerLog = victim.getName() + " (" + victim.getUniqueId() + ")";
                        SimpleBanHammer.getinstance().getLogger().warning("Admin " + adminLog + " has used SimpleBanHammer on player " + playerLog);
                        Bukkit.getScheduler().runTaskAsynchronously(SimpleBanHammer.getinstance(), () -> {
                            if (config.isSet("discord-webhook") && config.isString("discord-webhook")) {
                                Bukkit.getScheduler().runTask(SimpleBanHammer.getinstance(), () -> DiscordWebhook.sendWebhook(config.getString("discord-webhook"), victim, admin, reason));
                            }
                        });
                        // Checks weather 'enable-broadcast' is enabled
                        if (config.getBoolean("enable-broadcast")) {
                            // If enabled, sends every player a message of the ban
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
    // Gets the player's prefix for the broadcast message
    private static String getPlayerPrefix(Player player) {
        // Checks, if the luckperms is present on the server
        if (SimpleBanHammer.getLuckPerms()) {
            // If yes, it gets the prefix and returns it with player's name attached to it
            User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player);
            String prefix = user.getCachedData().getMetaData().getPrefix();

            if (prefix != null) {
                return prefix + player.getName();
            }
        } // If not, returns just the player name
        return player.getName();
    }
}
