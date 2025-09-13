package me.monkeee.simpleBanHammer.discord;

import lombok.Builder;

import java.util.List;

@Builder
public class WebhookPayload {
    private String content;
    private List<Embed> embeds;

    @Builder
    public static class Embed {
        private String title;
        private String description;
        private List<Field> fields;
    }

    @Builder
    public static class Field {
        private String name;
        private String value;
    }
}
