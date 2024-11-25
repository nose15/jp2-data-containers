package org.lukas;

import org.lukas.client.Client;
import org.lukas.message.model.Message;
import org.lukas.ui.InputManager;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args)
            throws IOException, InterruptedException {
        BlockingQueue<Message> messagesToSend = new LinkedBlockingQueue<>(2);

        InputManager inputManager = new InputManager(messagesToSend);
        Client client = new Client(messagesToSend);

        Thread producerThread = new Thread(inputManager);
        Thread consumerThread = new Thread(client);
        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
