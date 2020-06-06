package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Unban implements Command {
    @Override
    public String Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        User user = null;
        String returnValue = "NoLog";

        for (Guild.Ban ban : event.getGuild().retrieveBanList().complete()){
            if (ban.getUser().getId().equals(args[0])){
                user = ban.getUser();
                break;
            }
        }

        if (user != null){
            event.getGuild().unban(user).complete();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x8be041);
            embedBuilder.setTitle("User has been unbanned!");
            embedBuilder.setDescription(user.getName() + " has been unbanned!");

            event.getChannel().sendMessage(embedBuilder.build()).queue();
            returnValue = user.getName() + " was unbanned";
        } else {
            Main.sendErrorMessage("This user is not in the ban list!", event.getChannel());
        }

        return returnValue;
    }

    @Override
    public String getCommandName() {
        return "Unban";
    }

    @Override
    public String getDescription() {
        return "Unbans a user.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Unban** ```[user ID]```";
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
