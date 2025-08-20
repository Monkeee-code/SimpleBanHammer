package me.monkeee.simpleBanHammer.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        if (damager instanceof Player dmg && target instanceof Player victim) {
            if (dmg.hasPermission("sbh.usehammer") && !victim.isOp()) {
                ItemStack weapon = dmg.getInventory().getItemInMainHand();
                if (Objects.requireNonNull(weapon.getItemMeta()).getItemName().equals("sbh_hammer")) {
                    victim.getWorld().strikeLightning(victim.getLocation());
                    Bukkit.dispatchCommand(dmg, "ban " + victim.getName() + " The Ban Hammer Has Spoken!");
                    if (!victim.isOnline()) {
                        dmg.sendMessage(ChatColor.GREEN + "Player " + ChatColor.RESET + victim.getName() + ChatColor.GREEN + " has been banned!");
                    } else dmg.sendMessage(ChatColor.RED + "The ban failed. Please try again later.");
                }
            } else dmg.sendMessage(ChatColor.RED + "You don't have the right permissions or the player is Operator!");
        }
    }
}
