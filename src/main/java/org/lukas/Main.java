package org.lukas;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Path socketPath = Path.of("/home/lukasz").resolve("helloworld.socket");
        Path workingFile = Path.of("/home/lukasz/test");

        Server server = new Server(socketPath, workingFile);
        server.run();
    }
}
