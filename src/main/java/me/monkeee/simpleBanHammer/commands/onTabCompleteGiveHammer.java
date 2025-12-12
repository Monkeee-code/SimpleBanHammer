package me.monkeee.simpleBanHammer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class onTabCompleteGiveHammer implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Assign the reasons in confing to the variable
        List<String> list = new ArrayList<>();

        list.add("command:");
        list.add("reason:");

        String sArgs = String.join(" ", args);

        Matcher reasonMatcher = Pattern.compile("reason:\"([^\"]+)\"").matcher(sArgs);
        Matcher commandMatcher = Pattern.compile("command:\"([^\"]+)\"").matcher(sArgs);
        // Checks weather the sender is player
        if (sender instanceof Player) {
            // Checks if the command and the places of the cursor are correct
            if (command.getName().equalsIgnoreCase("givehammer")) {
                if (reasonMatcher.find()) list.remove("reason:");
                if (commandMatcher.find()) list.remove("command:");
                Collections.sort(list);
                return onTabCompleteConfig.getBetterList(list, args, 0);
            }
            if (args.length > 1)  return null;
            return null;
        }
        return null;
    }
}
