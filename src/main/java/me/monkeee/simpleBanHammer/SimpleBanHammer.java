package me.monkeee.simpleBanHammer;

import me.monkeee.simpleBanHammer.commands.giveDaHammer;
import me.monkeee.simpleBanHammer.events.PlayerHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SimpleBanHammer extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading Plugin");
        Objects.requireNonNull(this.getCommand("givehammer")).setExecutor(new giveDaHammer());
        getServer().getPluginManager().registerEvents(new PlayerHitEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
