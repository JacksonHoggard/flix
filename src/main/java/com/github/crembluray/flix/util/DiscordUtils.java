package com.github.crembluray.flix.util;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public class DiscordUtils {
    public static void reply(Message message, String text) {
        if (message.getAuthor().isPresent()) {
            message.getChannel().subscribe(c ->
                    c.createMessage(message.getAuthor().get().getMention() + " " + text).subscribe()
            );
        } else {
            message.getChannel().subscribe(c ->
                    c.createMessage(text).subscribe()
            );
        }
    }

    public static void sendMessage(Message to, String text) {
        to.getChannel().subscribe(c -> c.createMessage(text).subscribe());
    }

    public Mono<Boolean> isAuthorGuildOwner(Message message) {
        return message.getGuild()
                .flatMap(Guild::getOwner)
                .filter(member -> member.equals(message.getAuthor().orElse(null)))
                .hasElement();
    }
}