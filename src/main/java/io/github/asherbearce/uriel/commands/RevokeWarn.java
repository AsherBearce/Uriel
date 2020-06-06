package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.ResultSet;

public class RevokeWarn implements Command {
    @Override
    public String Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {

        String returnValue = "NoLog";
        int warnID = Integer.valueOf(args[0]);

        try{
            Database db = Database.getDatabase();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x8be041);
            embedBuilder.setTitle("Warning revoked!");
            ResultSet warnInfo = db.getUserWarning(warnID);
            String warnedUser = event.getGuild().getMemberById(warnInfo.getString(5)).getEffectiveName();
            String reason = warnInfo.getString(4);
            embedBuilder.addField("Username: ", warnedUser, false);
            embedBuilder.addField("Reason: ", reason, false);

            db.removeUserWarning(warnID);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            returnValue = "Warning was revoked for " + event.getGuild().getMemberById(warnedUser).getEffectiveName();
        } catch(Exception e){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("There's been a problem trying to obtain the warnings for this user!");
            embedBuilder.addField("Please try again, or contact the developer (Abstract_Math)", "", false);

            event.getChannel().sendMessage(embedBuilder.build()).queue();

        }

        return returnValue;
    }

    @Override
    public String getCommandName() {
        return "RevokeWarn";
    }

    @Override
    public String getDescription() {
        return "Removes a warning from the database, given a warning ID.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **RevokeWarn** ```[Warning id]```";
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
