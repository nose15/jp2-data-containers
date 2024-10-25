package org.lukas.socketservice;

import org.lukas.dtos.Message;
import org.lukas.parser.Parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketService {
    private SocketChannel socketChannel;

    public SocketService(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void send(Message message) throws IOException {
        ByteBuffer byteBuffer = Parser.encode(message);
        socketChannel.write(byteBuffer);
        byteBuffer.clear();
    }
}
