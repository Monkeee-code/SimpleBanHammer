package me.monkeee.simpleBanHammer.events;

import me.monkeee.simpleBanHammer.SimpleBanHammer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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

public class onJoin implements Listener {

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!player.isOp()) return;

        FileConfiguration config = SimpleBanHammer.getinstance().getConfig();
        String currVer = config.getString("current-version");

        // Run update check asynchronously
        Bukkit.getScheduler().runTaskAsynchronously(SimpleBanHammer.getinstance(), () -> {
            String latestVer = getLatestVersion();
            if (latestVer != null && !Objects.equals(currVer, latestVer)) {
                Bukkit.getScheduler().runTask(SimpleBanHammer.getinstance(), () -> {
                    player.sendMessage(ChatColor.GOLD + "A new version of SimpleBanHammer is available!" +
                            ChatColor.YELLOW + "\nCurrent Version: " + currVer +
                            ChatColor.GREEN + "\nLatest Version: " + latestVer);
                });
            }
        });
    }
}
