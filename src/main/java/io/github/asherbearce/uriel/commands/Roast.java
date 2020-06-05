package io.github.asherbearce.uriel.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Roast implements Command {
    @Override
    public String getCommandName() {
        return "Roast";
    }

    @Override
    public String getDescription() {
        return "A fun test command used to roast Sam.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Roast**";
    }

    @Override
    public void Execute(final JDA jda, final GuildMessageReceivedEvent event, final String[] args) {
        event.getChannel().sendMessage("Sam is a fucking bitch ass!").queue();
    }
}
