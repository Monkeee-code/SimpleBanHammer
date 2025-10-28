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
        // Assign all of the values to variables
        String[] list = {"set", "get", "help", "list", "reload"};
        String[] configNamesArr = {"enable-broadcast", "broadcast-message", "ban-command", "default-reason", "update-notifier", "item-banhammer", "enable-console-sender", "discord-webhook"};
        // Check if the sender is player
        if (sender instanceof Player) {
            // Check if its the correct command (name)
            if (command.getName().equalsIgnoreCase("sbh_config")) {
                // Check where the sender is
                if (args.length == 1) {
                    return getBetterList(Arrays.asList(list), args, 0);
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("get")) {
                        return getBetterList(Arrays.asList(configNamesArr), args, 1);
                    }
                }
                if (args.length == 3) {
                    // Check what is the config's name
                    if (args[1].equalsIgnoreCase("enable-broadcast") || args[1].equalsIgnoreCase("update-notifier")) {
                        List<String> bool = new ArrayList<>();
                        // Add value, based on the config type
                        bool.add("true");
                        bool.add("false");
                        return getBetterList(bool, args, 2);
                    }
                    if (args[1].equalsIgnoreCase("item-banhammer")) {
                        List<String> returnList = new ArrayList<>();
                        // Loop through all of the Minecraft items and add them to the list
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

    // Makes the auto complete look better and responsive
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
