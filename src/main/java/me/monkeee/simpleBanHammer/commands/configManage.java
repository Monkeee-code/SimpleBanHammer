package me.monkeee.simpleBanHammer.commands;

import me.monkeee.simpleBanHammer.SimpleBanHammer;
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
            sender.sendRichMessage("<red>You're missing arguments!</red>");
            return false;
        } else if (args[0].equalsIgnoreCase("list")) {
            sender.sendRichMessage(getConfigNames());
        } else if (args[0].equalsIgnoreCase("help")) {
            sender.sendRichMessage(getHelpStrings());
        } else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendRichMessage("<green>The config has been reloaded!");
        } else if (args[0].equalsIgnoreCase("get")) {
            if (args.length > 1) {
                String configName = args[1];
                if (config.isSet(configName)) {
                    // sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Current value of \"&r" + configName + ChatColor.GOLD + "\":&r\n" + config.get(configName)));
                    sender.sendRichMessage("<gold>Current value of \"<reset>" + configName + "<gold>\":<reset>\n" + String.valueOf(config.get(configName)).replace("&", "§"));
                } else sender.sendRichMessage("<red>There is no config by the name of: <reset>" + configName);
            } else sender.sendRichMessage("<red>Please provide a valid config option!");
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
                    sender.sendRichMessage("<green>Config <reset>" + configName + " <green>has been set to: \n<reset>" + newValue.replace('&', '§'));
                } else sender.sendRichMessage("<red>There is no config by the name of: <white>" + configName);
            } else sender.sendRichMessage("<red>Please provide a valid config option!");
        } else sender.sendRichMessage("<red>Unknow option for config managing!");
        return false;
    }

    @NotNull
    private static String getConfigNames() {
        List<String> configList = new ArrayList<>();
        configList.add("<yellow>List of config names:");
        configList.add("<dark_aqua>enable-broadcast  <white>- Defines if the public broadcast is on or off");
        configList.add("<dark_aqua>broadcast-message <white>- Defines the broadcast message");
        configList.add("<dark_aqua>ban-command <white>- Defines the command that is used when using the hammer! \n<green>Vars: %player%, %reason%");
        configList.add("<dark_aqua>default-reason <white>- Defines the default ban reason");
        configList.add("<dark_aqua>update-notifier <white>- Defines if Operators get notified of new plugin versions!");
        configList.add("<dark_aqua>webhook-link <white>- Defines where to send the logs of the bans. (Leave empty to not sand anything)");
        return String.join("\n", configList);
    }

    @NotNull
    private static String getHelpStrings() {
        List<String> s = new ArrayList<>();
        s.add("<yellow>List of config options:");
        s.add("<red>help <white>- Shows this message!");
        s.add("<red>list <white>- Shows the list of all config names");
        s.add("<red>set <white>- Sets a new value to the config");
        s.add("<red>get <white>- Gets the config's current value");
        s.add("<red>reload <white>- Reloads the config, applying any new changes!");
        return String.join("\n", s);
    }
}
