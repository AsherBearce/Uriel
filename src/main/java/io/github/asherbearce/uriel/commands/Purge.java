package io.github.asherbearce.uriel.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Purge implements Command {
    @Override
    public void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        int purgeAmount = Integer.valueOf(args[0]);
        MessageHistory history = event.getChannel().getHistoryBefore(event.getMessageId(), purgeAmount).complete();

        event.getChannel().deleteMessages(history.getRetrievedHistory()).complete();
        event.getChannel().sendMessage(purgeAmount + " messages have been deleted!").queue();
    }

    @Override
    public String getCommandName() {
        return "Purge";
    }
}
