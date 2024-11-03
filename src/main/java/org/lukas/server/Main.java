package org.lukas.server;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Options options = new Options();

        options.addOption("f", "file", true, "File on which the operations will be made");
        options.addOption("s", "socket", true, "UDS address");

        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Error while parsing arguments");
            System.exit(1);
        }

        Path socketPath = null;
        Path workingFile = null;

        if (cmd.hasOption("f")) {
            workingFile = Path.of(cmd.getOptionValue("f"));
        } else {
            System.out.println("File must be specified using -f (--file)");
            System.exit(1);
        }

        if (cmd.hasOption("s")) {
            socketPath = Path.of(cmd.getOptionValue("s"));
        } else {
            System.out.println("UDS address must be specified using -s (--socket)");
            System.exit(1);
        }

        Server server = new Server(socketPath, workingFile);
        server.run();
    }
}
