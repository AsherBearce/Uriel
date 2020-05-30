package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.internal.requests.Route;

public class Settings implements Command{
    @Override
    public String getCommandName() {
        return "Settings";
    }

    @Override
    public void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        if (args[0].equalsIgnoreCase("prefix")){
            Main.settings.setCommandPrefix(args[1]);
        } else if (args[0].equalsIgnoreCase("leavemessage")){
            Main.settings.setLeaveMessage(args[1]);
        } else if (args[0].equalsIgnoreCase("joinmessage")){
            Main.settings.setJoinMessage(args[1]);
        } else if (args[0].equalsIgnoreCase("welcomemessagechannelid")){
            Main.settings.setJoinLeaveTextChannelID(args[1]);
        } else if (args[0].equalsIgnoreCase("disablewelcomemessage")){
            Main.settings.setShowJoinLeaveMessage(false);
        } else if (args[0].equalsIgnoreCase("enablewelcomemessage")){
            Main.settings.setShowJoinLeaveMessage(true);
        }

        Main.UpdateSettings();
        event.getMessage().addReaction("\uD83D\uDC4C").queue();
    }
}
