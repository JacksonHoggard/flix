package com.github.crembluray.flix.module;

import com.github.crembluray.flix.command.Command;
import com.github.crembluray.flix.util.DiscordUtils;
import discord4j.core.object.entity.Message;

public class Ping extends Command {

    public Ping() {
        super("ping", "Command that replies to the sender with \"Pong!\"");
    }

    @Override
    public void onCommand(Message message, String[] args) throws Exception {
        DiscordUtils.sendMessage(message, "Pong!");
    }
}
