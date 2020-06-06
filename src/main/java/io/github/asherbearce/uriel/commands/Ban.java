package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Ban implements Command {
    @Override
    public void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        List<Member> members = event.getMessage().getMentionedMembers();
        Member mentioned = members.isEmpty() ? event.getGuild().getMemberById(args[0]) : members.get(0);

        if (mentioned != null){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("You have been banned!");
            embedBuilder.addField("Reason:", args[1], false);
            embedBuilder.setDescription("If you feel that this ban was unjustified, please contact an admin or moderator");
            embedBuilder.setColor(0x8b0000);
            mentioned.getUser().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();

            event.getGuild().ban(mentioned.getId(), 0, args[1]).complete();
        } else {
            Main.sendErrormesage("This user does not exist.", event.getChannel());
        }
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
