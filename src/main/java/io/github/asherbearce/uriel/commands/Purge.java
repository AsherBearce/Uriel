package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Purge implements Command {
    @Override
    public String Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        int purgeAmount;
        try {
            purgeAmount = Integer.valueOf(args[0]);
        } catch (Exception e){
            Main.sendErrorMessage("The given argument wasn't a number.", event.getChannel());
            return "NoLog";
        }
        MessageHistory history = event.getChannel().getHistoryBefore(event.getMessageId(), purgeAmount).complete();

        event.getChannel().deleteMessages(history.getRetrievedHistory()).complete();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Messages Purged!");
        embedBuilder.setDescription(purgeAmount + " messages have been deleted by " + event.getMember().getEffectiveName());

        event.getChannel().sendMessage(embedBuilder.build()).queue();

        return "Deleted " + purgeAmount + " messages from channel " + event.getChannel().getName();
    }

    @Override
    public String getCommandName() {
        return "Purge";
    }

    @Override
    public String getDescription() {
        return "Deletes the number of messages specified.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Purge** ```[Number of messages to delete]```";
    }

    @Override
    public int getMaxArguments() {
        return 1;
    }

    @Override
    public int getMinArguments() {
        return 1;
    }
}
