package me.monkeee.simpleBanHammer;

import de.tr7zw.changeme.nbtapi.NBT;
import me.monkeee.simpleBanHammer.commands.giveDaHammer;
import me.monkeee.simpleBanHammer.commands.reloadConfig;
import me.monkeee.simpleBanHammer.events.PlayerHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SimpleBanHammer extends JavaPlugin {
    private static SimpleBanHammer instance;

    @Override
    public void onEnable() {
        instance = this;
        if (!NBT.preloadApi()) {
            getLogger().warning("NBT-API wasn't initialized properly, disabling the plugin");
            getPluginLoader().disablePlugin(this);
            return;
        }
        // Plugin startup logic
        saveDefaultConfig();
        getLogger().info("Loading Plugin");
        Objects.requireNonNull(this.getCommand("givehammer")).setExecutor(new giveDaHammer());
        Objects.requireNonNull(this.getCommand("sbh_reload")).setExecutor(new reloadConfig());
        getServer().getPluginManager().registerEvents(new PlayerHitEvent(), this);
    }

    public static SimpleBanHammer getinstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
