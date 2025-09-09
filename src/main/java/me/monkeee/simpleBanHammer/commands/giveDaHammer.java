package me.monkeee.simpleBanHammer.commands;

import de.tr7zw.changeme.nbtapi.NBT;
import me.monkeee.simpleBanHammer.SimpleBanHammer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class giveDaHammer implements CommandExecutor {
    private String commandArgs(String[] args) {
        if (args.length > 0) {
            return String.join(" ", args);
        } else {
            return SimpleBanHammer.getinstance().getConfig().getString("default-reason");
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            FileConfiguration config = SimpleBanHammer.getinstance().getConfig();
            String ban_command = config.getString("ban-command");
            ItemStack bh = new ItemStack(Material.IRON_AXE);
            ItemMeta bhm = bh.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Reason:");
            lore.add(ChatColor.GRAY + commandArgs(strings));
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Command:");
            lore.add(ChatColor.GRAY + ban_command);
            assert bhm != null;
            bhm.setUnbreakable(true);
            bhm.setEnchantmentGlintOverride(true);
            bhm.setDisplayName(ChatColor.RED + "Ban Hammer");
            bhm.setItemName("sbh_hammer");
            bhm.setLore(lore);
            bh.setItemMeta(bhm);
            NBT.modify(bh, nbt -> {
                nbt.setString("Reason", commandArgs(strings));
            });

            if (commandSender.hasPermission("sbh.give") || commandSender.isOp()) {
                ((Player) commandSender).getInventory().addItem(bh);
                commandSender.sendMessage(ChatColor.GREEN + "You have been given the Ban Hammer!");
            } else {
                commandSender.sendMessage(ChatColor.RED + "Not Enough permission! :(");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "Only players can execute this command!");
        }
        return false;
    }
}
