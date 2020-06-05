package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.database.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class RevokeWarn implements Command {
    @Override
    public void Execute(JDA jda, GuildMessageReceivedEvent event, String[] args) {
        int warnID = Integer.valueOf(args[0]);
        try{
            Database db = Database.getDatabase();
            db.removeUserWarning(warnID);
            event.getChannel().sendMessage("The warning has been revoked!").queue();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getCommandName() {
        return "RevokeWarn";
    }

    @Override
    public String getDescription() {
        return "Removes a warning from the database, given a warning ID.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **RevokeWarn** ```[Warning id]```";
    }
}
