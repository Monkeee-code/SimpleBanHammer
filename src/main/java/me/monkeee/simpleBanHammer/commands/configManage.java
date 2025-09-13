package me.monkeee.simpleBanHammer.commands;

import me.monkeee.simpleBanHammer.SimpleBanHammer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class configManage implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SimpleBanHammer plugin = SimpleBanHammer.getinstance();
        FileConfiguration config = SimpleBanHammer.getinstance().getConfig();
        if (args.length == 0) {
            sender.sendMessage(Component.text("<red>You're missing arguments!</red>"));
            return false;
        } else if (args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(getConfigNames());
        } else if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(getHelpStrings());
        } else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "The config has been reloaded!");
        } else if (args[0].equalsIgnoreCase("get")) {
            if (args.length > 1) {
                String configName = args[1];
                if (config.isSet(configName)) {
                    // sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Current value of \"&r" + configName + ChatColor.GOLD + "\":&r\n" + config.get(configName)));
                    sender.sendMessage(Component.text(String.valueOf("&6Current value of \"&r" + configName + "&6\":&r\n" + config.get(configName)).replace('&', '§')));
                } else sender.sendMessage(Component.text("<red>There is no config by the name of: <reset>" + configName));
            } else sender.sendMessage(Component.text("<red>Please provide a valid config option!"));
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length > 2) {
                String configName = args[1];
                if (config.isSet(configName)) {
                    List<String> LinkedList = new ArrayList<>(Arrays.asList(args));
                    LinkedList.removeFirst();
                    LinkedList.removeFirst();
                    String[] modArgs = LinkedList.toArray(new String[0]);
                    String newValue = String.join(" ", modArgs);
                    config.set(configName, newValue);
                    plugin.saveConfig();
                    // sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aConfig &r" + configName + " &ahas been set to: \n&r" + ChatColor.translateAlternateColorCodes('&', newValue)));
                    sender.sendMessage(Component.text(String.valueOf("&aConfig &r" + configName + " &ahas been set to: \n&r" + newValue).replace('&', '§')));
                } else sender.sendMessage(Component.text("<red>There is no config by the name of: <white>" + configName));
            } else sender.sendMessage(Component.text("<red>Please provide a valid config option!"));
        } else sender.sendMessage(Component.text("<red>Unknow option for config managing!"));
        return false;
    }

    @NotNull
    private static String getConfigNames() {
        List<TextComponent> configList = new ArrayList<>();
        configList.add((Component.text("<yellow>List of config names:")));
        configList.add(Component.text( "<dark_aqua>enable-broadcast  <white>- Defines if the public broadcast is on or off"));
        configList.add(Component.text("<dark_aqua>broadcast-message <white>- Defines the broadcast message"));
        configList.add(ChatColor.DARK_AQUA + "ban-command" + ChatColor.WHITE + " - Defines the command that is used when using the hammer! \nVars: %player%, %reason%");
        configList.add(ChatColor.DARK_AQUA + "default-reason" + ChatColor.WHITE + " - Defines the default ban reason");
        configList.add(ChatColor.DARK_AQUA + "update-notifier" + ChatColor.WHITE + " - Defines if Operators get notified of new plugin versions!");
        configList.add(ChatColor.DARK_AQUA + "webhook-link" + ChatColor.WHITE + " - Defines where to send the logs of the bans. (Leave empty to not sand anything)");
        return String.join("\n", configList);
    }

    @NotNull
    private static String getHelpStrings() {
        List<String> s = new ArrayList<>();
        s.add("&eList of config options:");
        s.add("&chelp &r- Shows this message!");
        s.add("&clist &r- Shows the list of all config names");
        s.add("&cset &r- Sets a new value to the config");
        s.add("&cget &r- Gets the config's current value");
        s.add("&creload &r- Reloads the config, applying any new changes!");
        return ChatColor.translateAlternateColorCodes('&', String.join("\n", s));
    }
}
