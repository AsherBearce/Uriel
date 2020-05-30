package io.github.asherbearce.uriel.commands;

import java.util.LinkedList;
import java.util.List;

public class CommandList {
    public static List<Command> Commands = new LinkedList<>();

    static{
        Commands.add(new Roast());
        Commands.add(new Settings());
    }
}
