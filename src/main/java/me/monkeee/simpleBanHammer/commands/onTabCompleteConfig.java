package me.monkeee.simpleBanHammer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class onTabCompleteConfig implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String[] list = {"set", "get", "help", "list", "reload"};
        String[] configNamesArr = {"enable-broadcast", "broadcast-message", "ban-command", "default-reason", "update-notifier"};
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("sbh_config")) {
                if (args.length == 1) {
                    return Arrays.asList(list);
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("get")) {
                        return Arrays.asList(configNamesArr);
                    }
                }
                if (args.length == 3) {
                    if (args[1].equalsIgnoreCase("enable-broadcast") || args[1].equalsIgnoreCase("update-notifier")) {
                        List<String> bool = new ArrayList<>();
                        bool.add("true");
                        bool.add("false");
                        return bool;
                    }
                }
            }
        }
        return null;
    }
}
