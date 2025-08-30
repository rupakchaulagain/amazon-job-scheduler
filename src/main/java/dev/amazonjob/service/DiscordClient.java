package dev.amazonjob.service;

import dev.amazonjob.config.AppProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class DiscordClient {
    private static final Logger log = LoggerFactory.getLogger(DiscordClient.class);
    private final WebClient http;
    private final AppProps props;

    public DiscordClient(WebClient http, AppProps props) {
        this.http = http;
        this.props = props;
    }

    public void sendSimpleMessage(String content) {
        if (props.getDiscord().getWebhookUrl() == null || props.getDiscord().getWebhookUrl().length == 0) {
            log.warn("Discord webhook not configured, skipping message.");
            return;
        }
        log.info("Sending to discord message started");
        Map<String, Object> body = Map.of("content", content);

        List<String> webhookUrls = List.of(props.getDiscord().getWebhookUrl());

        webhookUrls.forEach(webhookUrl -> {
            http.post()
                    .uri(webhookUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorReturn("")
                    .block();
        });

        log.info("Sending to discord message completed");
    }
}
