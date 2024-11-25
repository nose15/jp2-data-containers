package org.lukas.server;

import org.apache.commons.cli.*;
import org.lukas.message.handler.AddHandler;
import org.lukas.message.handler.FindHandler;
import org.lukas.message.handler.GetAllHandler;
import org.lukas.message.handler.GetOneHandler;
import org.lukas.message.model.MessageType;
import org.lukas.server.router.Router;
import org.lukas.message.router.MessageTypeRouter;

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
        return Path
                .of(System.getProperty("user.home"))
                .resolve("hello.socket");
    }
}
