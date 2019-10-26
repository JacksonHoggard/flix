package com.github.crembluray.flix.command;

import discord4j.core.object.entity.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    public Map<String, Command> registered;
    public Map<String, Command> aliases;

    public CommandManager() {
        registered = new HashMap<>();
        aliases = new HashMap<>();
    }

    public void registerCommand(Command command) {
        this.registered.put(command.command, command);
        for (String cmd : command.aliases)
            aliases.put(cmd, command);
    }

    public Command getCommand(String cmd) {
        Command command = registered.get(cmd);
        if (command != null)
            return command;
        return aliases.get(cmd);
    }

    public void process(Message message) {
        String text = message.getContent().orElse("f!").substring(2);
        String[] args = text.split(" ");
        Command command = getCommand(args[0].toLowerCase());
        if (command == null)
            return;
        command.canUse(message).subscribe(can -> {
            if (can) {
                try {
                    command.onCommand(message, Arrays.copyOfRange(args, 1, args.length));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
