package com.github.crembluray.flix;

import com.github.crembluray.flix.command.CommandManager;
import com.github.crembluray.flix.module.Ping;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Flix {

    private static Flix instance;
    public static final Logger log = LoggerFactory.getLogger("Flix");

    public final DiscordClient client;

    private final CommandManager commandManager;

    private Flix()  {
        instance = this;

        client = new DiscordClientBuilder(readConfig()).build();
        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(this::onMessage);

        commandManager = new CommandManager(this);
        commandManager.registerCommand(new Ping());

        client.login().block();
    }

    private String readConfig() {
        BufferedReader objReader = null;
        String token = null;
        try {
            objReader = new BufferedReader(new FileReader("token.txt"));
            token = objReader.readLine();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    public void onMessage(MessageCreateEvent event) {
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
