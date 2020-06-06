package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import io.github.asherbearce.uriel.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.util.Date;

public class Warn implements Command {
    @Override
    //TODO Add error handling here
    public String Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        Member userToWarn = !event.getMessage().getMentionedMembers().isEmpty() ?
                event.getMessage().getMentionedMembers().get(0) : event.getGuild().getMemberById(args[0]);

        String returnValue = "NoLog";

        if (userToWarn != null) {
            giveUserWarn(userToWarn, event.getGuild(), new Date(), event.getMember().getId(), args[1].toLowerCase());
            returnValue = userToWarn.getEffectiveName() + " was warned. Reason: "+ args[1].toLowerCase();
        } else {
            Main.sendErrorMessage("This user does not exist.", event.getChannel());
        }

        return returnValue;
    }

    @Override
    public String getCommandName() {
        return "Warn";
    }

    @Override
    public String getDescription() {
        return "Warns a user. Sends the user a dm telling them why they were warned.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Warn** ```user mention or user ID``` ```[Reason]```";
    }

    @Override
    public int getMaxArguments() {
        return 2;
    }

    @Override
    public int getMinArguments() {
        return 2;
    }

    public static void giveUserWarn(Member member, Guild guild, Date date, String issuerID, String reason){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("You have been warned!");
        embedBuilder.addField("Reason: ", reason, false);
        embedBuilder.addField("Date: ", date.toString(), false);
        embedBuilder.addField("Issuer: ", guild.getMemberById(issuerID).getEffectiveName(), false);
        embedBuilder.setColor(0xe3d57d);
        embedBuilder.setDescription("This warning has been recorded in our database. If you feel that this warning has been wrongfully given to you, please contact a moderator.");

        member.getUser().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
        try {
            Database db = Database.getDatabase();
            db.addWarning(new Date(), issuerID, reason, member.getId());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
