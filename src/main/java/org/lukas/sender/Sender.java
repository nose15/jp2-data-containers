package org.lukas.sender;

import org.lukas.dtos.Message;
import org.lukas.parser.Parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

public class Sender {
    private static Sender instance;

    private SocketChannel socketChannel;

    private Sender() {}

    public Sender getInstance() {
        if (instance == null) {
            instance = new Sender();
        }

        return instance;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void send(Message message) throws IOException {
        // TODO: Figure out the correct size
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byte[] encodedMessage = Parser.encode(message);
        byteBuffer.put(encodedMessage);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }
}
