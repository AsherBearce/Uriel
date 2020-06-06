package io.github.asherbearce.uriel.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Help implements Command {
    @Override
    public String Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        String returnValue = "NoLog";

        if (args.length == 0){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Help menu");
            embedBuilder.addField("", "How to use the commands:", false);
            embedBuilder.addField("Command Grammar: ", "(prefix)[command name] (arguments)", false);
            embedBuilder.addField("Notes:", "Any argument that is more than one word must be contained in double quotes to be counted as a single argument.", false);
            embedBuilder.addField("", "Either a user ID, or a mention can be used for every argument that requires it.", false);
            embedBuilder.addField("Subcommands for help:", "", false);
            embedBuilder.addField("", "-Use the argument \"commands\" to get a list of commands.", false);
            embedBuilder.addField("", "-Use the name of the command after !Help to get more specifics on the command.", false);

            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } else if (args[0].equalsIgnoreCase("Commands")){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Command list:");

            for (Command c : CommandList.Commands){
                embedBuilder.addField(c.getCommandName(), c.getDescription(), false);
            }

            event.getChannel().sendMessage("A list of commands has been sent to you!").queue();
            event.getMember().getUser().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
        } else {
            String argName = args[0];
            Command command = CommandList.getCommandByName(argName);
            String argsRequired = command.getArgumentList();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Argument description of " + command.getCommandName());
            embedBuilder.addField(argsRequired, "", false);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }

        return returnValue;
    }

    @Override
    public String getCommandName() {
        return "Help";
    }

    @Override
    public String getDescription() {
        return "Gives a list of commands and how to use the bot.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Help** ```\"Commands\" or [Command Name Here]```";
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
