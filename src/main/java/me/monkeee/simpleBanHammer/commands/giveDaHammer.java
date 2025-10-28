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
import java.util.Objects;

public class giveDaHammer implements CommandExecutor {

    // Get the reason as a string insted of a Array
    private String commandArgs(String[] args) {
        if (args.length > 0) {
            return String.join(" ", args);
        } else {
            return SimpleBanHammer.getinstance().getConfig().getString("default-reason");
        }
    }
    // Main Function
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        // Checks if the sender is a player
        if (commandSender instanceof Player) {
            // Get the config instance of the plugin
            FileConfiguration config = SimpleBanHammer.getinstance().getConfig();
            // Get the ban command from the config
            String ban_command = config.getString("ban-command");
            // Create an Item from the config (if empty, defaults to an iron axe)
            ItemStack bh = (config.getString("item-banhammer") != null || !Objects.equals(config.getString("item-banhammer"), "")) ? new ItemStack(Material.valueOf(Objects.requireNonNull(config.getString("item-banhammer")).toUpperCase())) : new ItemStack(Material.IRON_AXE);
            // Creates the metadata for the new item
            ItemMeta bhm = bh.getItemMeta();
            // Assigns a new list for the lore
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.RED + "Reason" + ChatColor.WHITE + ":");
            lore.add(ChatColor.GREEN + commandArgs(strings));
            lore.add(" ");
            lore.add(ChatColor.RED + "Command" + ChatColor.WHITE + ":");
            lore.add(ChatColor.GREEN + ban_command);
            assert bhm != null;
            // Sets the items metadata to the appropriate 'settings'
            bhm.setUnbreakable(true);
            bhm.setEnchantmentGlintOverride(true);
            bhm.setDisplayName(ChatColor.RED + "The Ban Hammer");
            bhm.setItemName("sbh_hammer");
            bhm.setLore(lore);
            // Applies the Metadata to the item
            bh.setItemMeta(bhm);
            // Assigns the item the ban reason
            NBT.modify(bh, nbt -> {
                nbt.setString("Reason", commandArgs(strings));
            });
            // Checks if the sender is eligible for the item to be given to him
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
