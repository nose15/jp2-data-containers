package org.lukas;

import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;
import org.lukas.parser.Parser;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

public class Client {
    public static void main(String[] args)
            throws IOException, InterruptedException {
        Path socketFile = Path
                .of(System.getProperty("user.home"))
                .resolve("helloworld.socket");
        UnixDomainSocketAddress address =
                UnixDomainSocketAddress.of(socketFile);

        SocketChannel channel = SocketChannel
                .open(StandardProtocolFamily.UNIX);
        channel.connect(address);


        while (true) {
            for (var messageType : MessageType.values()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                Message message = new Message(messageType, "Siema eniu");
                buffer.put(Parser.encode(message));
                buffer.flip();
                channel.write(buffer);

                buffer.clear();
                channel.read(buffer);
                Message received = Parser.decode(buffer);
                System.out.println(received.getMessageType() + ": " + received.getContent());

                Thread.sleep(1000);
            }
        }
    }
}
