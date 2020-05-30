package io.github.asherbearce.uriel.commands;

import java.util.LinkedList;
import java.util.List;

public class CommandList {
    public static List<Command> Commands = new LinkedList<>();

    static{
        //Commands go here
        //commands.add(new Command());
        Commands.add(new Roast());
    }
}
