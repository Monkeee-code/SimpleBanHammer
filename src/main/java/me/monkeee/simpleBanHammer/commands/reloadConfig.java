package me.monkeee.simpleBanHammer.commands;

import me.monkeee.simpleBanHammer.SimpleBanHammer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class reloadConfig implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SimpleBanHammer instance = SimpleBanHammer.getinstance();
        if (sender.hasPermission("sbh.reload") || sender.isOp()) {
            instance.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "The SBH config has been reloaded!");
        } else sender.sendMessage(ChatColor.RED + "You don't have enough to reload the config!");
        return false;
    }
}
