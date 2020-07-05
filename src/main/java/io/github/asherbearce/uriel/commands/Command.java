package io.github.asherbearce.uriel.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import java.util.List;

public interface Command {
    enum PERMISSION_TYPES {ADMIN, NONE}

    String Execute(JDA jda, Guild guild, TextChannel channel, Member author, String[] args);
    String getCommandName();
    String getDescription();
    String getArgumentList();
    int getMaxArguments();
    int getMinArguments();

    default PERMISSION_TYPES getPermissions(){
        return PERMISSION_TYPES.ADMIN;
    }
}
