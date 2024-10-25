package org.lukas;

import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;
import org.lukas.handler.impl.OkMessageHandler;
import org.lukas.handler.impl.WriteMessageHandler;
import org.lukas.parser.Parser;
import org.lukas.router.Router;
import org.lukas.router.impl.MessageTypeRouter;
import org.lukas.sender.SocketService;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;

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

        SocketChannel channel = serverChannel.accept();

        SocketService socketService = new SocketService(channel);
        Router router = new MessageTypeRouter(socketService);
        router.setHandler(MessageType.OK, new OkMessageHandler());
        router.setHandler(MessageType.WRITE, new WriteMessageHandler());
        router.setHandler(MessageType.CLEAR, new ClearMessageHandler());
        router.setHandler(MessageType.ERROR, new ErrorMessageHandler());
        router.setHandler(MessageType.PING, new PingMessageHandler());

        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);
            // TODO: Parse buffer to message
            Message message = Parser.decode(buffer);
            System.out.println(message);
            buffer.clear();
            router.dispatch(message);

            Thread.sleep(100);
        }
    }
}