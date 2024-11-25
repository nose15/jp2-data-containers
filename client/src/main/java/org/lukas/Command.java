package org.lukas;

import org.lukas.serializers.CommandSerializer;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private CommandType keyword;
    private final List<String> args = new ArrayList<>();

    public Command(String keyword, List<String> args) {
        this.keyword = CommandType.valueOf(keyword);
        this.args.addAll(args);
    }

    public static Command fromString(String input) {
        return CommandSerializer.parseCommand(input);
    }

    public void setKeyword(CommandType keyword) {
        this.keyword = keyword;
    }

    public void addArg(String arg) {
        args.add(arg);
    }

    public CommandType getKeyword() {
        return keyword;
    }

    public List<String> getArgs() {
        return args;
    }
}