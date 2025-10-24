package me.monkeee.simpleBanHammer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.bukkit.entity.Player;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class DiscordWebhook {
    public static void sendWebhook(String webhookUrl, Player player, Player admin, String reason) {
        try {
            // Create the embed object
            JSONObject thumbnail = new JSONObject();
            thumbnail.put("url", "https://mc-heads.net/avatar/" + player.getName() +".png");
            JSONObject embed = new JSONObject();
            embed.put("title", "[SBH] Player Banned");
            embed.put("description", "A player has been banned from the server with a Ban Hammer.");
            embed.put("thumbnail", thumbnail);
            embed.put("color", 0xFF0000); // red color

            // Fields array
            JSONArray fields = new JSONArray();

            fields.put(new JSONObject()
                    .put("name", "Player")
                    .put("value", "**"+player.getName()+"**\n`("+player.getUniqueId()+")`")
                    .put("inline", true));

            fields.put(new JSONObject()
                    .put("name", "Admin")
                    .put("value", "**"+admin.getName()+"**\n`("+admin.getUniqueId()+")`")
                    .put("inline", true));

            fields.put(new JSONObject()
                    .put("name", "Reason")
                    .put("value", "**"+reason+"**")
                    .put("inline", false));

            embed.put("fields", fields);

            // Combine embeds
            JSONArray embeds = new JSONArray();
            embeds.put(embed);

            // Root JSON
            JSONObject json = new JSONObject();
            json.put("username", "[SBH] Logging");
            json.put("avatar_url", "https://mc-heads.net/avatar/" + admin.getName() +".png");
            json.put("embeds", embeds);

            // Send the request
            URL url = new URI(webhookUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.toString().getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 204) {
                SimpleBanHammer.getinstance().getLogger().info("✅ Embed sent successfully!");
            } else {
                SimpleBanHammer.getinstance().getLogger().warning("⚠️ Discord webhook returned code: " + responseCode);
            }

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
