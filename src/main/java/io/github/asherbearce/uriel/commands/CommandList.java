package io.github.asherbearce.uriel.commands;

import java.util.LinkedList;
import java.util.List;

public class CommandList {
    public static List<Command> Commands = new LinkedList<>();

    static{
        Commands.add(new Roast());
        Commands.add(new Settings());
        Commands.add(new Mute());
        Commands.add(new Unmute());
        Commands.add(new Purge());
        Commands.add(new Lockdown());
        Commands.add(new Warn());
        Commands.add(new GetWarns());
        Commands.add(new RevokeWarn());
        Commands.add(new Help());
        Commands.add(new Ban());
        Commands.add(new Unban());
    }

    public static Command getCommandByName(String commandName){
        for (Command c : Commands){
            if (c.getCommandName().equalsIgnoreCase(commandName)){
                return c;
            }
        }
        return null;
    }
}
