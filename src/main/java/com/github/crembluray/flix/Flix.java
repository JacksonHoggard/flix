package com.github.crembluray.flix;

import com.github.crembluray.flix.core.Bot;
import com.github.crembluray.flix.core.Command;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Flix {

    private static final Map<String, Command> commands = new HashMap<>();

    public static void main(String[] args) {
        BufferedReader objReader = null;
        String token = null;
        try {
            objReader = new BufferedReader(new FileReader("token.txt"));
            token = objReader.readLine();
        } catch(IOException e) {
            e.printStackTrace();
        }

        new Bot(token, commands);
        //client.login().block();
    }

    static {
        commands.put("ping", event -> event.getMessage()
                .getChannel().block()
                .createMessage("Pong!").block());
    }

}
