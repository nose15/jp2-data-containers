package org.lukas.server;

import org.lukas.server.filemanager.FileManager;
import org.lukas.server.handler.impl.*;
import org.lukas.parser.Parser;
import org.lukas.server.router.Router;
import org.lukas.server.router.impl.MessageTypeRouter;

import java.io.IOException;
import java.net.SocketException;
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
    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private final Router router;

    public Server(Path socketFile, Path filePath) throws IOException {
        Files.deleteIfExists(socketFile);
        FileManager fileManager = new FileManager(filePath);

        router = new MessageTypeRouter();
        router.setHandler(MessageType.OK, new OkMessageHandler());
        router.setHandler(MessageType.WRITE, new WriteMessageHandler(fileManager));
        router.setHandler(MessageType.CLEAR, new ClearMessageHandler(fileManager));
        router.setHandler(MessageType.ERROR, new ErrorMessageHandler());
        router.setHandler(MessageType.PING, new PingMessageHandler());

        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
        serverSocketChannel.bind(UnixDomainSocketAddress.of(socketFile));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    }

    public void run() throws IOException, InterruptedException {
        System.out.println("Server listening to messages");
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if(key.isAcceptable()) {
                    registerClient(serverSocketChannel);
                } else if (key.isReadable()) {
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    try {

                        processMessage(clientChannel, buffer);
                    } catch (SocketException e) {
                        if (clientChannel.isOpen()) {
                            clientChannel.close();
                        } else {
                            System.out.println("Suddenly closed connection with " + clientChannel.getLocalAddress());
                        }
                    }
                }
            }

            iterator.remove();
            Thread.sleep(100);
        }
    }

    private void processMessage(SocketChannel clientChannel, ByteBuffer buffer) throws IOException {
        int bytesRead = clientChannel.read(buffer);
        if (bytesRead <= 0 || new String(buffer.array()).trim().equals("POISON_PILL")) {
            clientChannel.close();
            System.out.println("Closed connection with " + clientChannel.getLocalAddress());
            return;
        }

        Message message = Parser.decode(buffer);
        buffer.clear();

        Optional<Message> response = router.dispatch(message);
        if (response.isPresent()) {
            sendResponse(clientChannel, response.get(), buffer);
        }
    }

    private void registerClient(ServerSocketChannel clientChannel)
            throws IOException {

        System.out.println("Registered client");
        SocketChannel client = clientChannel.accept();
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