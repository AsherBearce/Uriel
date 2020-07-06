package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Unmute implements Command {

    @Override
    public String Execute(JDA jda, Guild guild, TextChannel channel, Member author, String[] args) {
        Matcher m = Pattern.compile("[0-9]+").matcher(args[0]);
        m.find();
        Member member = guild.getMemberById(m.group());
        String returnValue = "NoLog";

        if (member != null) {
            unmuteUser(member, guild);
            returnValue = member.getEffectiveName() + " was unmuted";
        } else {
            Main.sendErrorMessage("This user does not exist.", channel);
        }

        return returnValue;
    }

    @Override
    public String getCommandName() {
        return "Unmute";
    }

    @Override
    public String getDescription() {
        return "Gives a user the ability to type and connect to voice channels.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **UnMute** ``` ```user mention or user ID```";
    }

    @Override
    public int getMaxArguments() {
        return 1;
    }

    @Override
    public int getMinArguments() {
        return 1;
    }

    public static void unmuteUser(Member member, Guild guild){
        Role mutedRole = guild.getRoleById(Main.settings.getMutedRoleID());
        List<Role> unmodifiedRoles = member.getRoles();

        List<Role> modifiedRoles = new LinkedList<>(unmodifiedRoles);
        modifiedRoles.remove(mutedRole);
        guild.modifyMemberRoles(member, modifiedRoles).complete();
    }
}
