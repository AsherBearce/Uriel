package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.database.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;
import java.util.Date;

public class Warn implements Command {
    @Override
    //TODO Add error handling here
    public void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        Member userToWarn = !event.getMessage().getMentionedMembers().isEmpty() ?
                event.getMessage().getMentionedMembers().get(0) : event.getGuild().getMemberById(args[0]);
        giveUserWarn(userToWarn, event.getGuild(), new Date(), event.getMember().getId(), args[1].toLowerCase());
    }

    @Override
    public String getCommandName() {
        return "Warn";
    }

    @Override
    public String getDescription() {
        return "Warns a user. Sends the user a dm telling them why they were warned.";
    }

    public static void giveUserWarn(Member member, Guild guild, Date date, String issuerID, String reason){
        member.getUser().openPrivateChannel().complete().sendMessage("You have been warned for " + reason.toLowerCase()).queue();
        try {
            Database db = Database.getDatabase();
            db.addWarning(new Date(), issuerID, reason, member.getId());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
