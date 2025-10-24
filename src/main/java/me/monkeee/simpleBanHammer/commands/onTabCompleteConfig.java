package me.monkeee.simpleBanHammer.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class onTabCompleteConfig implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String[] list = {"set", "get", "help", "list", "reload"};
        String[] configNamesArr = {"enable-broadcast", "broadcast-message", "ban-command", "default-reason", "update-notifier", "item-banhammer", "enable-console-sender", "discord-webhook"};
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("sbh_config")) {
                if (args.length == 1) {
                    return getBetterList(Arrays.asList(list), args, 0);
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("get")) {
                        return getBetterList(Arrays.asList(configNamesArr), args, 1);
                    }
                }
                if (args.length == 3) {
                    if (args[1].equalsIgnoreCase("enable-broadcast") || args[1].equalsIgnoreCase("update-notifier")) {
                        List<String> bool = new ArrayList<>();
                        bool.add("true");
                        bool.add("false");
                        return getBetterList(bool, args, 2);
                    }
                    if (args[1].equalsIgnoreCase("item-banhammer")) {
                        List<String> returnList = new ArrayList<>();
                        for (Material mat:Material.values()) {
                            returnList.add(String.valueOf(mat).toLowerCase());
                        }
                        return getBetterList(returnList, args, 2);
                    }
                }
            }
        }
        return null;
    }

    public static List<String> getBetterList(List<String> autoCompleteList, String[] args, int argStage) {
       List<String> completions = null;
       String input = args[argStage];
       for (String s : autoCompleteList) {
           if (s.toLowerCase().startsWith(input) || s.toUpperCase().startsWith(input)) {
               if (completions == null) {
                   completions = new ArrayList<>();
               }
               completions.add(s);
           }
       }
       if (completions != null) Collections.sort(completions);
       return completions;
    }
}
