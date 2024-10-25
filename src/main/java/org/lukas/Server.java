package org.lukas;

import sun.misc.Signal;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Server {
    public static void main(String[] args)
            throws IOException, InterruptedException {
        Path socketFile = Path
                .of(System.getProperty("user.home"))
                .resolve("helloworld.socket");
        // in case the file is left over from the last run,
        // this makes the demo more robust
        Files.deleteIfExists(socketFile);
        UnixDomainSocketAddress address =
                UnixDomainSocketAddress.of(socketFile);

        ServerSocketChannel serverChannel = ServerSocketChannel
                .open(StandardProtocolFamily.UNIX);
        serverChannel.bind(address);

        System.out.println("[INFO] Waiting for client to connect...");
        SocketChannel channel = serverChannel.accept();
        System.out.println("[INFO] Client connected");
    }
}