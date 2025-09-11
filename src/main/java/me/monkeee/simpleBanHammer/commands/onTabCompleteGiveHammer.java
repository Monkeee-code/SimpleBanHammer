package me.monkeee.simpleBanHammer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class onTabCompleteGiveHammer implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("givehammer") && args.length == 1) {
                    list.add("Breaking Rules...");
                    list.add("Bug Exploiting...");
                    list.add("Cheating...");
                    list.add("<reason>");
                    return list;

            }
        }
        return null;
    }
}
