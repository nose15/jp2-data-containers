package org.lukas.serializers;

import org.lukas.Command;

import java.util.Arrays;

public class CommandSerializer {
    public static Command parseCommand(String input) {
        String[] elements = input.split(" ");
        String keyword = elements[0].toUpperCase();
        Command command = new Command(keyword, Arrays.stream(elements).skip(1).toList());
        return command;
    }
}
