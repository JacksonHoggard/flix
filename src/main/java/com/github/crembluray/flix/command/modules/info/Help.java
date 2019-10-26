package com.github.crembluray.flix.command.modules.info;

import com.github.crembluray.flix.command.Command;
import com.github.crembluray.flix.command.CommandManager;
import com.github.crembluray.flix.util.DiscordUtils;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

public class Help extends Command {

    private final CommandManager commands;

    public Help(CommandManager commands) {
        super("help", "Replies with a list of all the commands and their functions.");
        this.commands = commands;
    }

    @Override
    public void onCommand(Message message, String[] args) {
        if (args.length != 0) {
            Command command = commands.getCommand(args[0]);
            if (command == null) {
                DiscordUtils.reply(message, "`No command found.`");
                return;
            }
            String msg;
            if (command.getHelp() != null)
                msg = "`" + command.getHelp() + "`";
            else
                msg = "`No help available for the " + command.getName() + " command`";
            DiscordUtils.sendMessage(message, msg);
            return;
        }

        Flux.fromIterable(commands.commandMap.values())
                .map(Command::getName)
                .reduce((prev, current) -> prev + ", " + current)
                .subscribe(commands -> {
                    commands = "\nCommands available to you:\n ```" + commands + "```";
                    commands += "\nType `!help <command>` to see a description of the command";
                    DiscordUtils.reply(message, commands);
                });
    }
}
