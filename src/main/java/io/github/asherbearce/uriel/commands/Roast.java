package io.github.asherbearce.uriel.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Roast implements Command {
    @Override
    public String getCommandName() {
        return "Roast";
    }

    @Override
    public void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        event.getChannel().sendMessage("Sam is a fucking bitch ass!").queue();
    }
}
