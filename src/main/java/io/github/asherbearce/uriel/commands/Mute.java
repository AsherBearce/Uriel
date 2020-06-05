package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class Mute implements Command {

    @Override
    public void Execute(final JDA jda, final GuildMessageReceivedEvent event, final String[] args) {
        //TODO Clean up is needed here
        List<Member> members = event.getMessage().getMentionedMembers();
        final Member mentioned = members.isEmpty() ? event.getGuild().getMemberById(args[0]) : members.get(0);

        if (args.length == 2) {

            String time = args[1];
            String[] split = time.split(":");
            int hours = Integer.valueOf(split[0]);
            int minutes = Integer.valueOf(split[1]);

            muteUser(mentioned, event.getGuild(), hours, minutes);
        }
        else {
            muteUser(mentioned, event.getGuild());
        }
    }

    @Override
    public String getCommandName() {
        return "Mute";
    }

    @Override
    public String getDescription() {
        return "Mutes a particular user, disabling their ability to type, or connect from any voice channel.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Mute** ```user mention or user ID``` ```[Time formatted as dd:hh:mm]```(Mutes the user for a set period of time)(optional)";
    }

    public static void muteUser(Member member, Guild guild){
        Role mutedRole = guild.getRoleById(Main.settings.getMutedRoleID());
        List<Role> unmodifiedRoles = member.getRoles();

        List<Role> modifiedRoles = new LinkedList<>(unmodifiedRoles);
        modifiedRoles.add(mutedRole);
        guild.modifyMemberRoles(member, modifiedRoles).complete();
    }

    public static void muteUser(final Member member, final Guild guild, int hours, int minutes){
        muteUser(member, guild);

        final int timeMilis = 3600000 * hours + 60000 * minutes;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeMilis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Unmute.unmuteUser(member, guild);
            }
        }).start();
    }
}
