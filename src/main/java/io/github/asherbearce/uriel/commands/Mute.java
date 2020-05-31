package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.LinkedList;
import java.util.List;

public class Mute implements Command {

    @Override
    public void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        //TODO Clean up is needed here
        final List<Member> mentioned = event.getMessage().getMentionedMembers();
        final Role mutedRole = event.getGuild().getRoleById(Main.settings.getMutedRoleID());
        final List<Role> normalRoles = mentioned.get(0).getRoles();

        List<Role> modifiedRoles = new LinkedList<>(normalRoles);
        modifiedRoles.add(mutedRole);

        event.getGuild().modifyMemberRoles(mentioned.get(0), modifiedRoles).complete();

        String time = args[1];
        String[] split = time.split(":");
        int hours = Integer.valueOf(split[0]);
        int minutes = Integer.valueOf(split[1]);

        final int timeMilis = 3600000 * hours + 60000 * minutes;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeMilis);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                for (Member member : mentioned){
                    event.getGuild().modifyMemberRoles(member, normalRoles).complete();
                }
            }
        }).start();
    }

    @Override
    public String getCommandName() {
        return "Mute";
    }
}
