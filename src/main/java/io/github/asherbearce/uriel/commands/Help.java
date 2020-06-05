package io.github.asherbearce.uriel.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Help implements Command {
    @Override
    public void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 0){
            //TODO finish the help menu
        } else if (args[0].equalsIgnoreCase("Commands")){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Command list:");

            for (Command c : CommandList.Commands){
                embedBuilder.addField(c.getCommandName(), c.getDescription(), false);
            }

            event.getChannel().sendMessage("A list of commands has been sent to you!").queue();
            event.getMember().getUser().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
        }
    }

    @Override
    public String getCommandName() {
        return "Help";
    }

    @Override
    public String getDescription() {
        return "Gives a list of commands and how to use the bot.";
    }
}
