package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.LinkedList;
import java.util.List;


public class Lockdown implements Command{
    @Override
    public String Execute(JDA jda, Guild guild, TextChannel channel, Member author, String[] args) {
        List<Permission> perms = new LinkedList<>();
        perms.add(Permission.MESSAGE_WRITE);
        perms.add(Permission.MESSAGE_ADD_REACTION);
        perms.add(Permission.MESSAGE_TTS);

        if (args.length == 0) {
            if (Main.isUnderLockdown){
                channel.sendMessage("The server is already on lockdown.").queue();
                return "NoLog";
            }
            Main.isUnderLockdown = true;
            for (GuildChannel Channel : guild.getChannels()) {
                for (Role role : guild.getRoles()) {
                    if (!role.hasPermission(Permission.ADMINISTRATOR) && role.hasPermission(Permission.MESSAGE_WRITE)) {
                        Channel.getManager().putPermissionOverride(role, null, perms).complete();
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("Lift")){
            if (!Main.isUnderLockdown){
                channel.sendMessage("The server is already not on lockdown.").queue();
                return "NoLog";
            }
            Main.isUnderLockdown = false;
            for (GuildChannel guildChannel : guild.getChannels()) {
                for (Role role : guild.getRoles()) {
                    if (!role.hasPermission(Permission.ADMINISTRATOR) && !role.hasPermission(Permission.MESSAGE_WRITE)) {
                        channel.getManager().putPermissionOverride(role, perms, null).complete();
                    }
                }
            }
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Server locked down!");
        builder.setDescription("No one is able to type");

        channel.sendMessage(builder.build()).queue();
        return Main.isUnderLockdown ? "Server was locked down" : "Server lockdown was lifted";
    }

    @Override
    public String getCommandName() {
        return "Lockdown";
    }

    @Override
    public String getDescription() {
        return "Locks down the entire server, making it impossible for users to type, with the exception of moderators " +
                "and admins.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Lockdown** \"Lift\"(Lifts the lockdown)(optional)";
    }

    @Override
    public int getMaxArguments() {
        return 1;
    }

    @Override
    public int getMinArguments() {
        return 0;
    }
}
