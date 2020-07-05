package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ban implements Command {
    @Override
    public String Execute(JDA jda, Guild guild, TextChannel channel, Member author, String[] args) {
        //Do some argument processing.
        Matcher m = Pattern.compile("[0-9]*").matcher(args[0]);
        Member member = guild.getMemberById(m.group());
        String reason = "";

        String returnValue = "Command Error";

        if (member!= null){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("You have been banned!");
            embedBuilder.addField("Reason:", reason, false);
            embedBuilder.setDescription("If you feel that this ban was unjustified, please contact an admin or moderator");
            embedBuilder.setColor(0x8b0000);
            member.getUser().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();


            returnValue = "Banned " + member.getEffectiveName() + " Reason: " + reason;

            guild.ban(member.getId(), 0, reason).complete();
        } else {
            Main.sendErrorMessage("This user does not exist.", channel);
        }

        return returnValue;
    }

    @Override
    public String getCommandName() {
        return "Ban";
    }

    @Override
    public String getDescription() {
        return "Bans a user from the server.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Ban** ```[user mention or user ID]``` ```[Reason]```";
    }

    @Override
    public int getMaxArguments() {
        return 2;
    }

    @Override
    public int getMinArguments() {
        return 2;
    }
}
