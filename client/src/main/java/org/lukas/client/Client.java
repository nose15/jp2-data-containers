package org.lukas.client;

import org.lukas.message.model.Message;
import org.lukas.message.model.MessageType;
import org.lukas.message.serializers.MessageParser;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class Client implements Runnable {
private final BlockingQueue<Message> messages;

    public Client(BlockingQueue<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        try {
            Path socketFile = Path
                    .of(System.getProperty("user.home"))
                    .resolve("hello.socket");
            UnixDomainSocketAddress address =
                    UnixDomainSocketAddress.of(socketFile);

            SocketChannel channel = SocketChannel
                    .open(StandardProtocolFamily.UNIX);
            channel.connect(address);

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            Selector selector = Selector.open();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

            BlockingQueue<String> commands;

            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isReadable()) {
                        int readBytes = channel.read(buffer);
                        if (readBytes > 0) {
                            Message read = MessageParser.decode(buffer);
                            System.out.println(read.getMessageType() + ": " + read.getContent());
                            buffer.clear();
                        }
                    }

                    if (key.isWritable() && !messages.isEmpty()) {
                        Message message = messages.take();
                        buffer.put(MessageParser.encode(message));
                        buffer.flip();
                        channel.write(buffer);
                        buffer.clear();
                    }
                }

                iterator.remove();
                Thread.sleep(100);
            }
        } catch (ClosedChannelException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendMessage(Message message) {

    }
}
