package com.github.crembluray.flix.command.modules.info;

import com.github.crembluray.flix.command.Command;
import com.github.crembluray.flix.command.CommandManager;
import com.github.crembluray.flix.util.DiscordUtils;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

public class Help extends Command {

    private final CommandManager commands;

    public Help(CommandManager commands) {
        super("help", "Replies with a list of all the commands and their functions.");
        this.commands = commands;
    }

    @Override
    public void onCommand(Message message, String[] args) throws Exception {
        if (args.length != 0) {
            Command command = commands.getCommand(args[0]);
            if (command == null) {
                DiscordUtils.reply(message, "`No command found.`");
                return;
            }
            String msg;
            if (command.help != null)
                msg = "`" + command.help + "`";
            else
                msg = "`No help available for the " + command.command + " command`";
            DiscordUtils.sendMessage(message, msg);
            return;
        }

        Flux.mergeSequential(
                commands.registered.values()
                        .stream()
                        .map(cmd -> cmd.canUse(message))
                        .collect(Collectors.toList()))
                .zipWithIterable(commands.registered.values())
                .reduce("", (str, tuple) -> {
                    if (tuple.getT1())
                        return str + ", " + tuple.getT2().command;
                    return str;
                })
                .subscribe(text -> {
                    text = text.substring(2);
                    text = "\nCommands available to you:\n ```" + text + "```";
                    text += "\nType `!help <command>` to see a description of the command";
                    DiscordUtils.reply(message, text);
                });
    }
}
