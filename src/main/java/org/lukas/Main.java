package org.lukas;

import org.apache.commons.cli.*;
import org.lukas.server.Server;
import org.lukas.server.handler.impl.AddHandler;
import org.lukas.server.handler.impl.FindHandler;
import org.lukas.server.handler.impl.GetAllHandler;
import org.lukas.server.handler.impl.GetOneHandler;
import org.lukas.server.message.MessageType;
import org.lukas.server.router.Router;
import org.lukas.server.router.impl.MessageTypeRouter;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Path socketPath = parseSocketPath(args);

        if (socketPath == null) {
            System.out.println("No socket path specified, exiting...");
            System.exit(-1);
        }

        Server server = new Server(socketPath);
        Router<MessageType> router = new MessageTypeRouter();
        server.setRouter(router);

        router.setHandler(MessageType.ADD, new AddHandler());
        router.setHandler(MessageType.GET_ALL, new GetAllHandler());
        router.setHandler(MessageType.GET_ONE, new GetOneHandler());
        router.setHandler(MessageType.FIND, new FindHandler());

        server.run();
    }


    private static Path parseSocketPath(String[] args) {
        Options options = new Options();

        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Error while parsing arguments");
            System.exit(1);
        }

        Path socketPath = null;

        if (cmd.hasOption("s")) {
            socketPath = Path.of(cmd.getOptionValue("s"));
        } else {
            System.out.println("UDS address must be specified using -s (--socket)");
            System.exit(1);
        }

        return socketPath;
    }
}


