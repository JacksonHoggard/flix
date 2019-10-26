package com.github.crembluray.flix.command;

import discord4j.core.object.entity.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    private final String name;
    private final String help;
    private final List<String> aliases = new ArrayList<>();

    protected Command(String name, String help) {
        this.name = name;
        this.help = help;
    }

    public abstract void onCommand(Message message, String[] args);

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public List<String> getAliases() {
        return List.copyOf(aliases);
    }
}