package io.github.asherbearce.uriel.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Roast implements Command {
    @Override
    public String getCommandName() {
        return "Roast";
    }

    @Override
    public String getDescription() {
        return "A fun test command used to roast Sam.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Roast**";
    }

    @Override
    public int getMaxArguments() {
        return 0;
    }

    @Override
    public int getMinArguments() {
        return 0;
    }

    @Override
    public String Execute(JDA jda, Guild guild, TextChannel channel, Member author, String[] args) {
        channel.sendMessage("Sam is a fucking bitch ass!").queue();
        return "Roasted Sam";
    }

    @Override
    public Command.PERMISSION_TYPES getPermissions(){
        return PERMISSION_TYPES.NONE;
    }
}
