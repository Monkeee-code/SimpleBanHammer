package me.monkeee.simpleBanHammer;

import de.tr7zw.changeme.nbtapi.NBT;
import me.monkeee.simpleBanHammer.commands.giveDaHammer;
import me.monkeee.simpleBanHammer.commands.configManage;
import me.monkeee.simpleBanHammer.commands.onTabCompleteConfig;
import me.monkeee.simpleBanHammer.events.PlayerHitEvent;
import me.monkeee.simpleBanHammer.events.onJoin;
import me.monkeee.simpleBanHammer.commands.onTabCompleteGiveHammer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SimpleBanHammer extends JavaPlugin {
    private static SimpleBanHammer instance;
    private static String lastVer;
    private static boolean LuckPermsEnabled = false;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> lastVer = getLatestVersion());
        if (!NBT.preloadApi()) {
            getLogger().warning("NBT-API wasn't initialized properly, disabling the plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            try {
                LuckPerms luckPermsAPI = LuckPermsProvider.get();
                LuckPermsEnabled = true;
            } catch (IllegalStateException e) {
                LuckPermsEnabled = false;
            }
        }
        int pluginId = 	27320;
        Metrics metrics = new Metrics(this, pluginId);

        saveDefaultConfig();
        ConfigUpdater.updateConfig(this, "config.yml");
//        getConfig().options().copyDefaults(true);
//        saveConfig();

        List<String> header = new ArrayList<>();
        header.add("######################################");
        header.add("#       SimpleBanHammer " + getDescription().getVersion() +"v       #");
        header.add("#             By _Monkeee            #");
        header.add("######################################");
        getConfig().options().setHeader(header);
        saveConfig();

        getLogger().info("Loading Plugin");
        Objects.requireNonNull(this.getCommand("givehammer")).setExecutor(new giveDaHammer());
        Objects.requireNonNull(this.getCommand("sbh_config")).setExecutor(new configManage());
        Objects.requireNonNull(getCommand("givehammer")).setTabCompleter(new onTabCompleteGiveHammer());
        Objects.requireNonNull(getCommand("sbh_config")).setTabCompleter(new onTabCompleteConfig());
        getServer().getPluginManager().registerEvents(new PlayerHitEvent(), this);
        getServer().getPluginManager().registerEvents(new onJoin(), this);
    }

    public static SimpleBanHammer getinstance() {
        return instance;
    }
    public static String getLastVer() { return lastVer; }

    public static boolean getLuckPerms() {
        return LuckPermsEnabled;
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
