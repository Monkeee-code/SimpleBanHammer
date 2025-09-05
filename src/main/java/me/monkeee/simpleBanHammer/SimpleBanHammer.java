package me.monkeee.simpleBanHammer;

import de.tr7zw.changeme.nbtapi.NBT;
import me.monkeee.simpleBanHammer.commands.giveDaHammer;
import me.monkeee.simpleBanHammer.commands.reloadConfig;
import me.monkeee.simpleBanHammer.events.PlayerHitEvent;
import me.monkeee.simpleBanHammer.events.onJoin;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

public final class SimpleBanHammer extends JavaPlugin {
    private static SimpleBanHammer instance;
    private static String lastVer;

    @Override
    public void onEnable() {
        instance = this;
        lastVer = getLatestVersion();
        if (!NBT.preloadApi()) {
            getLogger().warning("NBT-API wasn't initialized properly, disabling the plugin");
            getPluginLoader().disablePlugin(this);
            return;
        }
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
        }
        saveDefaultConfig();
        getLogger().info("Loading Plugin");
        Objects.requireNonNull(this.getCommand("givehammer")).setExecutor(new giveDaHammer());
        Objects.requireNonNull(this.getCommand("sbh_reload")).setExecutor(new reloadConfig());
        getServer().getPluginManager().registerEvents(new PlayerHitEvent(), this);
        getServer().getPluginManager().registerEvents(new onJoin(), this);
    }

    public static SimpleBanHammer getinstance() {
        return instance;
    }
    public static String getLastVer() {
        return lastVer;
    }


    private String getLatestVersion() {
        try {
            URL url = new URI("https://api.modrinth.com/v2/project/simplebanhammer_me/version").toURL();
            JSONArray data = getObjects(url);
            JSONObject arr = data.getJSONObject(0);
            return arr.getString("version_number");
        } catch (Exception ex) {
            SimpleBanHammer.getinstance().getLogger().warning("Failed to fetch latest version: " + ex.getMessage());
            return null;
        }
    }

    @NotNull
    private static JSONArray getObjects(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "SimpleBanHammer Update Checker");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        return new JSONArray(content.toString());
    }


    @Override
    public void onDisable() {
        getLogger().warning("Thank you for using SimpleBanHammer! :D");
    }
}
