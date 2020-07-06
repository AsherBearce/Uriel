package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Purge implements Command {
    @Override
    public String Execute(JDA jda, Guild guild, TextChannel channel, Member author, String[] args) {
        int purgeAmount;
        try {
            purgeAmount = Integer.valueOf(args[0]);
        } catch (Exception e){
            Main.sendErrorMessage("The given argument wasn't a number.", channel);
            return "NoLog";
        }
        MessageHistory history = channel.getHistoryBefore(channel.getLatestMessageId(), purgeAmount).complete();

        channel.deleteMessages(history.getRetrievedHistory()).complete();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Messages Purged!");
        embedBuilder.setDescription(purgeAmount + " messages have been deleted by " + author.getEffectiveName());

        channel.sendMessage(embedBuilder.build()).queue();

        return "Deleted " + purgeAmount + " messages from channel " + channel.getName();
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
