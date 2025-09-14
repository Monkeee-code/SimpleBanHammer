package me.monkeee.simpleBanHammer.events;

import de.tr7zw.changeme.nbtapi.NBT;
import me.monkeee.simpleBanHammer.SimpleBanHammer;
import me.monkeee.simpleBanHammer.discord.Discord;
import me.monkeee.simpleBanHammer.discord.WebhookPayload;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class PlayerHitEvent implements Listener {
    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity target = e.getEntity();
        FileConfiguration config = SimpleBanHammer.getinstance().getConfig();
        String banCommand = config.getString("ban-command");
        assert banCommand != null;

        if (damager instanceof Player dmg && target instanceof Player victim) {

            if (dmg.hasPermission("sbh.usehammer") && !victim.isOp() && !victim.hasPermission("sbh.exampt")) {
                ItemStack weapon = dmg.getInventory().getItemInMainHand();
                String reason = NBT.get(weapon, nbt -> {
                    return nbt.getString("Reason");
                });
                banCommand = banCommand.replace("%player%", victim.getName());
                banCommand = banCommand.replace("%reason%", reason);
                if (Objects.requireNonNull(weapon.getItemMeta()).itemName().equals(Component.text("sbh_hammer"))) {
                    victim.getWorld().strikeLightning(victim.getLocation());
                    Bukkit.dispatchCommand(dmg, banCommand);
                    if (!victim.isOnline()) {
                        dmg.sendRichMessage("<green>Player <reset>" + victim.getName() + " <green>has been banned with Reason: <white>" + reason);
                        if (config.isSet("webhook-link")) {
                            if (Objects.requireNonNull(config.getString("webhook-link")).startsWith("https://discord.com")) {
                                sendLog(dmg, victim, reason, banCommand);
                            } else SimpleBanHammer.getinstance().getLogger().warning("Webhook URL in the config is not a valid discord link!");
                        }
                        if (config.getBoolean("enable-broadcast")) {
                            User admin = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(dmg);
                            User player = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(victim);
                            String PrefixAdmin = admin.getCachedData().getMetaData().getPrefix();
                            String PrefixPlayer = player.getCachedData().getMetaData().getPrefix();
                            for (Player users : Bukkit.getOnlinePlayers()) {
                                String message = config.getString("broadcast-message");
                                assert message != null;
                                message = message.replace("%player%", victim.getName());
                                message = message.replace("%prefix_player%", PrefixPlayer + victim.getName());
                                message = message.replace("%admin%", dmg.getName());
                                message = message.replace("%prefix_admin%", PrefixAdmin + dmg.getName());
                                message = message.replace("%reason%", reason);
                                message = message.replace("&", "§");
                                users.sendRichMessage(message);
                            }
                        }
                    } else dmg.sendRichMessage("<red>The ban failed. Please try again later.");
                }
            } else dmg.sendRichMessage("<red>You don't have the right permissions or the player is Operator!");
        }
    }

    public void sendLog(Player admin, Player player, String reason, String command) {
        final List<WebhookPayload.Embed> embeds = List.of(WebhookPayload.Embed.builder()
                .title("SBH Log")
                .description("Summery:\nAdmin " + admin.getName() + " banned player " + player.getName() + "\n\n")
                .fields(List.of(WebhookPayload.Field.builder()
                        .name("Admin:")
                        .value(admin.getName() + "\n(" + admin.getUniqueId() + ")")
                        .build(),
                        WebhookPayload.Field.builder()
                                .name("Player:")
                                .value(player.getName() + "\n(" + player.getUniqueId() + ")")
                                .build(),
                        WebhookPayload.Field.builder()
                                .name("Reason:")
                                .value(reason)
                                .build(),
                        WebhookPayload.Field.builder()
                                .name("Command Used:")
                                .value(command)
                                .build()
                ))
                .build()
        );
        final WebhookPayload payload = WebhookPayload.builder()
                .embeds(embeds)
                .build();
        Discord.sendMessage(payload);
    }
}
