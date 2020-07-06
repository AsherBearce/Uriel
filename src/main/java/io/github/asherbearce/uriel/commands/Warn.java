package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import io.github.asherbearce.uriel.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Warn implements Command {
    @Override
    //TODO Add error handling here
    public String Execute(JDA jda, Guild guild, TextChannel channel, Member author, String[] args) {
        Matcher m = Pattern.compile("[0-9]+").matcher(args[0]);
        m.find();
        Member userToWarn = guild.getMemberById(m.group());

        String reason = "";

        for (int i = 1; i < args.length; i++){
            reason += (args[i] + " ");
        }

        String returnValue = "NoLog";

        if (userToWarn != null) {
            giveUserWarn(userToWarn, guild, new Date(), author.getId(), reason.toLowerCase());
            returnValue = userToWarn.getEffectiveName() + " was warned. Reason: " + reason.toLowerCase();
        } else {
            Main.sendErrorMessage("This user does not exist.", channel);
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
        return 100;
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
