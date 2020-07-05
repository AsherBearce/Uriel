package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

//TODO this needs a revamp
public class Settings implements Command{
    @Override
    public String getCommandName() {
        return "Settings";
    }

    @Override
    public String getDescription() {
        return "Allows a user to change general settings for this bot.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Settings** ```[Setting Name]```(use ```prefix```**Settings** for a list of settings)";
    }

    @Override
    public int getMaxArguments() {
        return 2;
    }

    @Override
    public int getMinArguments() {
        return 0;
    }

    @Override
    public String Execute(final JDA jda, , final String[] args) {

        String returnValue = "NoLog";


        return returnValue;
    }
}
