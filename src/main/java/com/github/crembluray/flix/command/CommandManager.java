package com.github.crembluray.flix.command;

import discord4j.core.object.entity.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    public Map<String, Command> commandMap;

    public CommandManager() {
        commandMap = new HashMap<>();
    }

    public void registerCommand(Command command) {
        this.commandMap.put(command.getName(), command);
        for (String cmd : command.getAliases())
            commandMap.put(cmd, command);
    }

    public Command getCommand(String cmd) {
        return commandMap.get(cmd);
    }

    public void process(Message message) {
        String text = message.getContent().orElse("f!").substring(2);
        String[] args = text.split(" ");

        Command command = getCommand(args[0].toLowerCase());
        if (command == null)
            return;

        try {
            command.onCommand(message, Arrays.copyOfRange(args, 1, args.length));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
