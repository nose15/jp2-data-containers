package org.lukas;

import org.lukas.dtos.Message;
import org.lukas.enums.MessageType;
import org.lukas.handler.impl.*;
import org.lukas.parser.Parser;
import org.lukas.router.Router;
import org.lukas.router.impl.MessageTypeRouter;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class Server {
    public static void main(String[] args)
            throws IOException, InterruptedException {
        Path socketFile = Path
                .of(System.getProperty("user.home"))
                .resolve("helloworld.socket");
        Files.deleteIfExists(socketFile);

        Router router = new MessageTypeRouter();
        router.setHandler(MessageType.OK, new OkMessageHandler());
        router.setHandler(MessageType.WRITE, new WriteMessageHandler());
        router.setHandler(MessageType.CLEAR, new ClearMessageHandler());
        router.setHandler(MessageType.ERROR, new ErrorMessageHandler());
        router.setHandler(MessageType.PING, new PingMessageHandler());

        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
        serverSocketChannel.bind(UnixDomainSocketAddress.of(socketFile));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        System.out.println("Server listening to messages on " + socketFile.toString());

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if(key.isAcceptable()) {
                    registerClient(selector, serverSocketChannel);
                } else if (key.isReadable()) {
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    Message message = readMessage(clientChannel, buffer);
                    Optional<Message> response = router.dispatch(message);
                    if (response.isPresent()) {
                        sendResponse(clientChannel, response.get(), buffer);
                    }
                }
            }

            iterator.remove();
            Thread.sleep(100);
        }
    }

    private static Message readMessage(SocketChannel clientChannel, ByteBuffer temp) throws IOException {
        clientChannel.read(temp);
        Message message = Parser.decode(temp);
        temp.clear();

        return message;
    }

    private static void registerClient(Selector selector, ServerSocketChannel serverSocketChannel)
            throws IOException {

        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }


    private static void sendResponse(SocketChannel clientChannel, Message message, ByteBuffer buffer) throws IOException {
        buffer.put(Parser.encode(message));
        buffer.flip();
        clientChannel.write(buffer);
        buffer.clear();
    }
}