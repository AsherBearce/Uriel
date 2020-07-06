package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetWarns implements Command {
    @Override
    public String Execute(JDA jda, Guild guild, TextChannel channel, Member author, String[] args) {
        Matcher m = Pattern.compile("[0-9]+").matcher(args[0]);
        m.find();
        Member member = guild.getMemberById(m.group());

        String returnValue = "NoLog";

        try {
            Database db = Database.getDatabase();
            ResultSet results = db.getAllUserWarnings(member.getId());
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Results:");

            boolean isEmpty = true;

            while (results.next()){
                isEmpty = false;
                int warningID = results.getInt(1);
                String dateIssued = (LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(results.getDate(2).getTime()), ZoneId.systemDefault())).toString();
                String issuerName = guild.getMemberById(results.getString(3)).getEffectiveName();
                String reason = results.getString(4);

                String fieldMessage = "Warning ID: " + warningID + " Date Issued: " + dateIssued + " Issued by: " + issuerName + " Reason: " + reason;
                embedBuilder.addField(fieldMessage, "", false);
            }

            if (isEmpty){
                embedBuilder.addField("No results found", "", false);
            }

            MessageEmbed embed = embedBuilder.build();
            channel.sendMessage(embed).queue();
        } catch (Exception e){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("There's been a problem trying to obtain the warnings for this user!");
            embedBuilder.addField("Please try again, or contact the developer (Abstract_Math)", "", false);

           channel.sendMessage(embedBuilder.build()).queue();
        }

        return returnValue;
    }

    @Override
    public String getCommandName() {
        return "GetWarns";
    }

    @Override
    public String getDescription() {
        return "Gets the warns of a particular user.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **GetWarns** ```[user mention or user ID]```";
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
