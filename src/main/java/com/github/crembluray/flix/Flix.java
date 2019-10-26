package com.github.crembluray.flix;

import com.github.crembluray.flix.command.CommandManager;
import com.github.crembluray.flix.command.modules.info.Help;
import com.github.crembluray.flix.command.modules.info.Wiki;
import com.github.crembluray.flix.command.modules.utility.Ping;
import com.github.crembluray.flix.command.modules.utility.Screenshare;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Flix {
    private static Flix instance;
    private static final Logger log = LoggerFactory.getLogger("Flix");
    private final DiscordClient client;
    private final CommandManager commandManager;

    private Flix() {
        instance = this;

        client = new DiscordClientBuilder(readConfig()).build();
        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(this::onMessage);

        commandManager = new CommandManager(this);
        commandManager.registerCommand(new Ping());
        commandManager.registerCommand(new Wiki());
        commandManager.registerCommand(new Screenshare());
        commandManager.registerCommand(new Help(commandManager));

        client.login().block();
    }

    private String readConfig() {
        try {
            File tokenFile = new File("token.txt");
            return new BufferedReader(new FileReader(tokenFile)).readLine();
        } catch (IOException e) {
            log.error("token.txt does not exist or cannot be read. Please create it.");
            System.exit(1);
            return null;
        }
    }

    private void onMessage(MessageCreateEvent event) {
        event.getMessage().getContent().ifPresent(content -> {
            if (content.startsWith("f!"))
                commandManager.process(event.getMessage());
        });
    }

    public static void main(String[] args) {
        new Flix();
    }

    public static Flix getInstance() {
        return instance;
    }
}
