package io.github.asherbearce.uriel.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class RevokeWarn implements Command {
    @Override
    public void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {

    }

    @Override
    public String getCommandName() {
        return "RevokeWarn";
    }
}
