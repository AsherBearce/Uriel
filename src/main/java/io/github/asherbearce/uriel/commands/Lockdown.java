package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.LinkedList;
import java.util.List;


public class Lockdown implements Command{
    @Override
    public void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        List<Permission> perms = new LinkedList<>();
        perms.add(Permission.MESSAGE_WRITE);
        perms.add(Permission.MESSAGE_ADD_REACTION);
        perms.add(Permission.MESSAGE_TTS);

        if (args.length == 0) {
            if (Main.isUnderLockdown){
                event.getChannel().sendMessage("The server is already on lockdown.").queue();
                return;
            }
            Main.isUnderLockdown = true;
            for (GuildChannel channel : event.getGuild().getChannels()) {
                for (Role role : event.getGuild().getRoles()) {
                    if (!role.hasPermission(Permission.ADMINISTRATOR) && role.hasPermission(Permission.MESSAGE_WRITE)) {
                        channel.getManager().putPermissionOverride(role, null, perms).complete();
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("Lift")){
            if (!Main.isUnderLockdown){
                event.getChannel().sendMessage("The server is already not on lockdown.").queue();
                return;
            }
            for (GuildChannel channel : event.getGuild().getChannels()) {
                for (Role role : event.getGuild().getRoles()) {
                    if (!role.hasPermission(Permission.ADMINISTRATOR) && !role.hasPermission(Permission.MESSAGE_WRITE)) {
                        channel.getManager().putPermissionOverride(role, perms, null).complete();
                    }
                }
            }
        }

        event.getMessage().addReaction("\uD83D\uDC4C").queue();
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
