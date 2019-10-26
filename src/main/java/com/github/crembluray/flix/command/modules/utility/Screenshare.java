package com.github.crembluray.flix.command.modules.utility;

import com.github.crembluray.flix.command.Command;
import com.github.crembluray.flix.util.DiscordUtils;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class Screenshare extends Command {

    private static final String REPLY_NO_VOICE_CHANNEL = "You are not in a voice channel. Please join a voice channel to use the `screenshare` command.";

    public Screenshare() {
        super("screenshare", "Generates a screenshare link.");
    }

    @Override
    public void onCommand(Message message, String[] args) {
        message.getAuthorAsMember()
                // Get ID of user's voice channel
                .flatMap(Member::getVoiceState)
                .map(VoiceState::getChannelId)
                // Exit if user is not in a channel
                .filter(Optional::isPresent)
                .switchIfEmpty(Mono.fromRunnable(() -> DiscordUtils.reply(message, REPLY_NO_VOICE_CHANNEL)))
                // Build and send screenshare link
                .map(Optional::get)
                .map(Snowflake::asString)
                .subscribe(channelId -> {
                    String guildId = message.getGuild()
                            .map(Guild::getId)
                            .map(Snowflake::asString)
                            .block();
                    DiscordUtils.reply(message, "https://discordapp.com/channels/" + guildId + "/" + channelId);
                });
    }
}
