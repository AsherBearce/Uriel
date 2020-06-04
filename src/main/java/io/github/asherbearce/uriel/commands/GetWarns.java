package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.ResultSet;
import java.util.List;

public class GetWarns implements Command {
    @Override
    public void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        List<Member> mentioned = event.getMessage().getMentionedMembers();
        String userID = mentioned.isEmpty() ? args[0] : mentioned.get(0).getId();
        try {
            Database db = Database.getDatabase();
            ResultSet results = db.getAllUserWarnings(userID);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Results from Database query:");

            while (results.next()){
                int warningID = results.getInt(1);
                String dateIssued = results.getDate(2).toLocalDate().toString();
                String issuerName = event.getGuild().getMemberById(results.getString(3)).getEffectiveName();
                String reason = results.getString(4);

                String fieldMessage = "Warning ID: " + warningID + " Date Issued: " + dateIssued + " Reason: " + reason;
                embedBuilder.addField(fieldMessage, "", false);
            }

            MessageEmbed embed = embedBuilder.build();
            event.getChannel().sendMessage(embed).queue();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getCommandName() {
        return "GetWarns";
    }
}
