package me.monkeee.simpleBanHammer.commands;

import me.monkeee.simpleBanHammer.SimpleBanHammer;
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
        // Gets the plugins instance
        SimpleBanHammer plugin = SimpleBanHammer.getinstance();
        // Gets the plugin's config instance
        FileConfiguration config = SimpleBanHammer.getinstance().getConfig();

        // Checks if the sender has input any arguments
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "You're missing arguments!");
            return false;
        } else if (args[0].equalsIgnoreCase("list")) {
            // Executes the getConfigNames() function and sends it to the sender
            sender.sendMessage(getConfigNames());
        } else if (args[0].equalsIgnoreCase("help")) {
            // Executes the getHelpStrings() function and sends it to the sender
            sender.sendMessage(getHelpStrings());
        } else if (args[0].equalsIgnoreCase("reload")) {
            // Reloads the config
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "The config has been reloaded!");
        } else if (args[0].equalsIgnoreCase("get")) {
            if (args.length > 1) {
                // Gets the config's name
                String configName = args[1];
                // Checks if the config by that name is set
                if (config.isSet(configName)) {
                    // sends the sender the current value of the config by the config's name
                    sender.sendMessage(ChatColor.GOLD + "Current value of \"" + ChatColor.WHITE + configName + ChatColor.GOLD + "\":\n" + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', String.valueOf(config.get(configName))));
                } else sender.sendMessage(ChatColor.RED + "There is no config by the name of: " + ChatColor.WHITE + configName);
            } else sender.sendMessage(ChatColor.RED + "Please provide a valid config option!");
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length > 2) {
                // Gets the config's name
                String configName = args[1];
                // Cheks if the config is set
                if (config.isSet(configName)) {
                    // Assigns the command arguments as a list
                    List<String> LinkedList = new ArrayList<>(Arrays.asList(args));
                    // Removes the first 2 arguments form the list as they are not intended to be of use
                    LinkedList.removeFirst();
                    LinkedList.removeFirst();
                    // Assigns the modified list to an array
                    String[] modArgs = LinkedList.toArray(new String[0]);
                    // Joins the array together
                    String newValue = String.join(" ", modArgs);
                    // Sets the config's value based on the type (If its string or a boolean)
                    if (newValue.equalsIgnoreCase("false") || newValue.equalsIgnoreCase("true")) config.set(configName, Boolean.getBoolean(newValue)); else config.set(configName, newValue);
                    // Saves the new config values
                    plugin.saveConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aConfig &r" + configName + " &ahas been set to: \n&r" + ChatColor.translateAlternateColorCodes('&', newValue)));
                } else sender.sendMessage(ChatColor.RED + "There is no config by the name of: " + ChatColor.WHITE + configName);
            } else sender.sendMessage(ChatColor.RED + "Please provide a valid config option!");
        } else sender.sendMessage(ChatColor.RED + "Unknow option for config managing!");
        return false;
    }

    // All Config names (Hard-coded) in a single function, for easier retrival
    @NotNull
    private static String getConfigNames() {
        List<String> configList = new ArrayList<>();
        configList.add(ChatColor.YELLOW + "List of config names:");
        configList.add(ChatColor.DARK_AQUA + "enable-broadcast  " + ChatColor.WHITE +"- Defines if the public broadcast is on or off");
        configList.add(ChatColor.DARK_AQUA + "broadcast-message " + ChatColor.WHITE +"- Defines the broadcast message");
        configList.add(ChatColor.DARK_AQUA + "ban-command " + ChatColor.WHITE +"- Defines the command that is used when using the hammer! \n" + ChatColor.GREEN +"Vars: %player%, %reason%");
        configList.add(ChatColor.DARK_AQUA + "default-reason " + ChatColor.WHITE +"- Defines the default ban reason");
        configList.add(ChatColor.DARK_AQUA + "update-notifier " + ChatColor.WHITE +"- Defines if Operators get notified of new plugin versions!");
        configList.add(ChatColor.DARK_AQUA + "item-banhammer " + ChatColor.WHITE +"- Defines what item to use for the ban hammer");
        configList.add(ChatColor.DARK_AQUA + "enable-console-sender " + ChatColor.WHITE +"- Defines if the ban command should be executed by the admin or the console" + ChatColor.RED + " (BYPASSES PERMISSION)");
        configList.add(ChatColor.DARK_AQUA + "discord-webhook " + ChatColor.WHITE +"- Defines in what discord channel will the log be sant to" + ChatColor.RED + " (LEAVE EMPTY IF UNUSED)");
        return String.join("\n", configList);
    }
    // All the 'help' values (Hard-coded) in a single function, for the easier retrival
    @NotNull
    private static String getHelpStrings() {
        List<String> HelpList = new ArrayList<>();
        HelpList.add(ChatColor.YELLOW + "List of config options:");
        HelpList.add(ChatColor.RED + "help " + ChatColor.WHITE +"- Shows this message!");
        HelpList.add(ChatColor.RED + "list " + ChatColor.WHITE +"- Shows the list of all config names");
        HelpList.add(ChatColor.RED + "set " + ChatColor.WHITE +"- Sets a new value to the config");
        HelpList.add(ChatColor.RED + "get " + ChatColor.WHITE +"- Gets the config's current value");
        HelpList.add(ChatColor.RED + "reload " + ChatColor.WHITE +"- Reloads the config, applying any new changes!");
        return String.join("\n", HelpList);
    }
}
