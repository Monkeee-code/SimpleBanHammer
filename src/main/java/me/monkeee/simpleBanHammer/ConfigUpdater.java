package me.monkeee.simpleBanHammer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConfigUpdater {

    public static void updateConfig(JavaPlugin plugin, String fileName) {
        // Gets the config file with the parent folder and config file's name
        File configFile = new File(plugin.getDataFolder(), fileName);
        // Checks if it exists
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
            return;
        }

        try {
            // Loads server's current configuration
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            // Assigns the default/new configuration as an InputStream
            InputStream defConfigStream = plugin.getResource(fileName);
            // Checks if the default/new configuration is null
            if (defConfigStream == null) return;
            // Assigns the default configuration as an YamlConfiguration
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));

            boolean changed = false;
            // Loops through all the config names and values
            for (String key : defConfig.getKeys(true)) {
                // Checks if something has changed (If yes, flags the boolean as true)
                if (!config.contains(key)) {
                    config.set(key, defConfig.get(key));
                    changed = true;
                }
            }
            // If the boolean is true, the config change occurs
            if (changed) {
                config.save(configFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
