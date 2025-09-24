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
        File configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
            return;
        }

        try {
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            InputStream defConfigStream = plugin.getResource(fileName);
            if (defConfigStream == null) return;
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));

            boolean changed = false;
            for (String key : defConfig.getKeys(true)) {
                if (!config.contains(key)) {
                    config.set(key, defConfig.get(key));
                    changed = true;
                }
            }

            if (changed) {
                config.save(configFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
