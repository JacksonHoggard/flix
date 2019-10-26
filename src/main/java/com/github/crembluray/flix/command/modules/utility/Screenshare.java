package com.github.crembluray.flix.command.modules.utility;

import com.github.crembluray.flix.command.Command;
import com.github.crembluray.flix.util.DiscordUtils;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Message;
import discord4j.core.object.util.Snowflake;

import java.util.Optional;

public class Screenshare extends Command {

    private static final String REPLY_NO_VOICE_CHANNEL = "You are not in a voice channel. Please join a voice channel to use the `screenshare` command.";

    public Screenshare() {
        super("screenshare", "Generates a screenshare link.");
    }

    @Override
    public void onCommand(Message message, String[] args) throws Exception {
        Optional<Snowflake> channelIdOptional = message.getAuthorAsMember().block().getVoiceState().map(VoiceState::getChannelId).block();
        if (Optional.empty().equals(channelIdOptional)) {
            DiscordUtils.reply(message, REPLY_NO_VOICE_CHANNEL);
            return;
        }

        DiscordUtils.reply(message, "https://discordapp.com/channels/" + message.getGuild().block().getId().asLong() + "/" + channelIdOptional.get().asString());
    }
}
