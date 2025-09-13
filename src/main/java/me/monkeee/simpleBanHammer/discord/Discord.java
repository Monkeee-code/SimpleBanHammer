package me.monkeee.simpleBanHammer.discord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.monkeee.simpleBanHammer.SimpleBanHammer;
import org.bukkit.configuration.file.FileConfiguration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class Discord {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final FileConfiguration config = SimpleBanHammer.getinstance().getConfig();
    private static final String WEBHOOK_URL = config.getString("webhook-link");

    public static void sendMessage(WebhookPayload payload) {
        try (final HttpClient client = HttpClient.newHttpClient()) {
            final String json = gson.toJson(payload);
            assert WEBHOOK_URL != null;
            final HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-type", "application/json")
                    .uri(URI.create(WEBHOOK_URL))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            final CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            future.thenAccept(response -> System.out.println(response.statusCode()));
        }
    }
}
