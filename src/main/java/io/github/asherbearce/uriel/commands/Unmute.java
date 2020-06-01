package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.LinkedList;
import java.util.List;

public class Unmute implements Command {

    @Override
    public void Execute(final JDA jda, final GuildMessageReceivedEvent event, final String[] args) {
        List<Member> members = event.getMessage().getMentionedMembers();
        final Member mentioned = members.isEmpty() ? event.getGuild().getMemberById(args[0]) : members.get(0);

        unmuteUser(mentioned, event.getGuild());
    }

    @Override
    public String getCommandName() {
        return "Unmute";
    }

    public static void unmuteUser(Member member, Guild guild){
        Role mutedRole = guild.getRoleById(Main.settings.getMutedRoleID());
        List<Role> unmodifiedRoles = member.getRoles();

        List<Role> modifiedRoles = new LinkedList<>(unmodifiedRoles);
        modifiedRoles.remove(mutedRole);
        guild.modifyMemberRoles(member, modifiedRoles).complete();
    }
}
