package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.EmbedBuilder;
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
    public String getDescription() {
        return "Allows a user to change general settings for this bot.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Settings** ```[Setting Name]```(use ```prefix```**Settings** for a list of settings)";
    }

    @Override
    public int getMaxArguments() {
        return 0;
    }

    @Override
    public int getMinArguments() {
        return 2;
    }

    @Override
    public void Execute(final JDA jda, final GuildMessageReceivedEvent event, final String[] args) {

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case ("prefix"):
                    Main.settings.setCommandPrefix(args[1]);
                    break;
                case ("leavemessage"):
                    Main.settings.setLeaveMessage(args[1]);
                    break;
                case ("joinmessage"):
                    Main.settings.setJoinMessage(args[1]);
                    break;
                case ("welcomemessagechannelid"):
                    Main.settings.setJoinLeaveTextChannelID(args[1]);
                    break;
                case ("sendwelcomemessage"): {
                    try {
                        Main.settings.setShowJoinLeaveMessage(Boolean.valueOf(args[1].toLowerCase()));
                    } catch (Exception e){
                        Main.sendErrormesage("input not formatted correctly. Must either be 'true' or 'false'.", event.getChannel());
                    }
                    break;
                }
                case ("muterole"):
                    Main.settings.setMutedRoleID(args[1]);
                    break;
                default:
                    event.getChannel().sendMessage(args[0] + " is not a setting!").queue();
            }

            Main.UpdateSettings();
            event.getMessage().addReaction("\uD83D\uDC4C").queue();
        } else {
            //Send a list of settings
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("List of settings: ");
            embedBuilder.addField("","-Prefix: the prefix used when commanding the bot. Default: !", false);
            embedBuilder.addField("", "-LeaveMessage: the message sent when a user leaves the server. Default: none", false);
            embedBuilder.addField("","-JoinMessage: the message sent when a user joins the server. Default: none", false);
            embedBuilder.addField("", "-WelcomeMessageChannelID: the channel id of the channel used when greeting new users. Default: none", false);
            embedBuilder.addField("", "-SendWelcomeMessage: a value that determines if a greeting message is sent. Default: false", false);
            embedBuilder.addField("", "-MuteRole: the role id of the role to be given to muted users. Default: none", false);

            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }
}
