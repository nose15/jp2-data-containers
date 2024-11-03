package org.lukas.client;

import org.lukas.parser.Parser;
import org.lukas.server.Message;
import org.lukas.server.MessageType;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;

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

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        Selector selector = Selector.open();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        while (true) {
            for (var messageType : MessageType.values()) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isReadable()) {
                        int readBytes = channel.read(buffer);
                        if (readBytes > 0) {
                            Message read = Parser.decode(buffer);
                            System.out.println(read.getMessageType() + ": " + read.getContent());
                            buffer.clear();
                        }
                    }

                    if (key.isWritable()) {
                        Message message = new Message(messageType, "ding");
                        buffer.put(Parser.encode(message));
                        buffer.flip();
                        channel.write(buffer);
                        buffer.clear();
                    }
                }

                iterator.remove();
                Thread.sleep(100);
            }
        }
    }
}
