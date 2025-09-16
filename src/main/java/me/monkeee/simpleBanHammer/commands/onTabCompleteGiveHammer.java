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
        FileConfiguration config = SimpleBanHammer.getinstance().getConfig();
        List<String> list = config.getStringList("reasons");
        if (sender instanceof Player) {
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
