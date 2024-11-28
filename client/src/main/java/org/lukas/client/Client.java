package org.lukas.client;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lukas.decision.model.Importance;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
                            if (read.getMessageType() == MessageType.OK) {
                                displayMessage(read);
                            } else {
                                System.out.println(read.getMessageType() + ": " + read.getContent());
                            }
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

    private void displayMessage(Message message) {
        if (message.getContentLength() == 0) {
            return;
        }
        JSONObject content = new JSONObject(message.getContent());

        if (content.isEmpty()) {
            return;
        }

        String type = content.getString("type");

        if (type.equals("single")) {
            displaySingleDecision(content.getJSONObject("decision"));
        } else if (type.equals("multiple")) {
            JSONArray decisions = content.getJSONArray("decisions");

            for (Object decision : decisions) {
                JSONObject dec = (JSONObject) decision;
                displaySingleDecision(dec);
            }
        }
    }

    private void displaySingleDecision(JSONObject decision) {
        System.out.println("Decision " + decision.get("id") + ": ");
        System.out.println("Component " + decision.get("component"));
        System.out.println("Added on " + decision.get("date"));
        System.out.println("Made by " + decision.get("person"));
        System.out.println("Priority " + decision.get("importance"));
        System.out.println("Description " + decision.get("description") + "\n");
    }
}
