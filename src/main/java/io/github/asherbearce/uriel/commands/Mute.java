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

public class Mute implements Command {

    @Override
    public String Execute(JDA jda, Guild guild, TextChannel channel, Member author, String[] args) {
        Matcher m = Pattern.compile("[0-9]+").matcher(args[0]);
        m.find();
        Member member = guild.getMemberById(m.group());
        String returnValue = "NoLog";



        if (args.length >= 2) {
            int hours = 0;
            int minutes = 0;
            Matcher argMatcher = Pattern.compile("[0-9]+").matcher(args[1].toLowerCase());
            argMatcher.find();


            if (args[1].toLowerCase().indexOf(args[1].length()) == 'h'){
                hours = Integer.parseInt(argMatcher.group());
            } else {
                minutes = Integer.parseInt(argMatcher.group());
            }

            if (args.length == 3){
                argMatcher = Pattern.compile("[0-9]+").matcher(args[2].toLowerCase());

                if (args[2].toLowerCase().indexOf(args[2].length()) == 'm'){
                    hours = Integer.parseInt(argMatcher.group());
                } else {
                    minutes = Integer.parseInt(argMatcher.group());
                }
            }


            muteUser(member, guild, hours, minutes);
            returnValue = member.getEffectiveName() + " was been muted for: " + args[1];
        }
        else {
            muteUser(member, guild);
        }

        return returnValue;
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

    @Override
    public int getMaxArguments() {
        return 3;
    }

    @Override
    public int getMinArguments() {
        return 1;
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
