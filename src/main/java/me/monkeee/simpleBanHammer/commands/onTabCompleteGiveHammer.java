package me.monkeee.simpleBanHammer.commands;

import me.monkeee.simpleBanHammer.SimpleBanHammer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class onTabCompleteGiveHammer implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Get the config instance
        FileConfiguration config = SimpleBanHammer.getinstance().getConfig();
        // Assign the reasons in confing to the variable
        List<String> list = config.getStringList("reasons");
        // Checks weather the sender is player
        if (sender instanceof Player) {
            // Checks if the command and the places of the cursor are correct
            if (command.getName().equalsIgnoreCase("givehammer") && args.length == 1) {
                    list.add("<reason>");
                    Collections.sort(list);
                    return onTabCompleteConfig.getBetterList(list, args, 0);

            }
            if (args.length > 1)  return null;
            return null;
        }
        return null;
    }
}
