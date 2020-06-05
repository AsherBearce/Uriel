package io.github.asherbearce.uriel.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Command {
    void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args);
    String getCommandName();
    String getDescription();
}
