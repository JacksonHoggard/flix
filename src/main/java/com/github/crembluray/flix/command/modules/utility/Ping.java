package com.github.crembluray.flix.command.modules.utility;

import com.github.crembluray.flix.command.Command;
import com.github.crembluray.flix.util.DiscordUtils;
import discord4j.core.object.entity.Message;

public class Ping extends Command {

    public Ping() {
        super("ping", "Command that replies to the sender with \"Pong!\"");
    }

    @Override
    public void onCommand(Message message, String[] args) {
        DiscordUtils.sendMessage(message, "Pong!");
    }
}
