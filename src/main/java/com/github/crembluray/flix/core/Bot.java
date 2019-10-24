package com.github.crembluray.flix.core;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.Map;

public class Bot {

    private final DiscordClient bot;

    public Bot(String token, Map<String, Command> commands) {
        bot = createClient(token, commands);
    }

    private DiscordClient createClient(String token, Map<String, Command> commands) {
        DiscordClient client = new DiscordClientBuilder(token).build();
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .subscribe(event -> {
                    final String content = event.getMessage().getContent().orElse("");
                    for (final Map.Entry<String, Command> entry : commands.entrySet()) {
                        if (content.startsWith("f!" + entry.getKey())) {
                            entry.getValue().execute(event);
                            break;
                        }
                    }
                });
        client.login().block();
        return client;
    }

    public DiscordClient getBot() {
        return bot;
    }

}
