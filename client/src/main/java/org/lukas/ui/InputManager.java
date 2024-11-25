package org.lukas.ui;

import org.json.JSONObject;
import org.lukas.Command;
import org.lukas.message.model.Message;
import org.lukas.message.model.MessageType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputManager implements Runnable {
    private final BlockingQueue<Message> messages;

    public InputManager(BlockingQueue<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                try {
                    System.out.print("> ");
                    Command command = Command.fromString(scanner.nextLine());
                    handleCommand(command);
                    Thread.sleep(200);
                } catch (IllegalArgumentException e) {
                    System.out.println("Wrong command, use HELP");
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCommand(Command command) throws InterruptedException {
        switch (command.getKeyword()) {
            case ADD -> addingForm(command);
            case GET -> validateAndSendGet(command);
            case SEARCH -> validateAndSendSearch(command);
            case HELP -> showHelp();
            case EXIT -> System.exit(1);
        }
    }

    private void addingForm(Command command) throws InterruptedException {
        JSONObject jsonObject = new JSONObject();
        Scanner scanner = new Scanner(System.in);
        System.out.print("component <str>: ");
        jsonObject.put("component", scanner.nextLine());


        System.out.print("date <dd-MM-yyyy> (empty - now)");
        String dateStr = scanner.nextLine();

        while (!dateValid(dateStr)) {
            System.out.print("Wrong format - date <dd-MM-yyyy> (empty - now)");
            dateStr = scanner.nextLine();
        }

        if (dateStr == "") {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = currentDate.format(formatter);
            jsonObject.put("date", formattedDate);
        } else {
            jsonObject.put("date", dateStr);
        }

        System.out.print("Person name <str>: ");
        jsonObject.put("personId", scanner.nextLine());

        System.out.print("Importance <str> (MINOR/MAJOR/CRITICAL) (empty - MINOR): ");
        String importanceString = scanner.nextLine();
        while (!importanceValid(importanceString)) {
            System.out.print("Wrong data - Importance <str> (MINOR/MAJOR/CRITICAL) (empty - MINOR): ");
            importanceString = scanner.nextLine();
        }
        if (importanceString.isEmpty()) {
            jsonObject.put("importance", "MINOR");
        } else {
            jsonObject.put("importance", importanceString.toUpperCase());
        }

        System.out.print("description (str): ");
        jsonObject.put("description", scanner.nextLine());

        Message message = new Message(MessageType.ADD, jsonObject.toString());
        messages.put(message);
    }

    private void validateAndSendGet(Command command) throws InterruptedException {
        if (command.getArgs().isEmpty()) {
            Message message = new Message(MessageType.GET_ALL);
            messages.put(message);
        } else if (command.getArgs().size() == 1) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", command.getArgs().get(0));
            Message message = new Message(MessageType.GET_ONE, jsonObject.toString());
            messages.put(message);
        } else {
            System.out.println("Invalid argument count, should be 0 or 1 (id)");
        }
    }

    private void validateAndSendSearch(Command command) throws InterruptedException {
        List<String> queries = command.getArgs();
        JSONObject jsonObject = new JSONObject();

        for (var query : queries) {
            String[] elements = query.split("=");
            if (elements.length != 2) {
                System.out.println("Invalid format, should be: SEARCH <column>=<value> <column>=<value> ...");
                return;
            }

            jsonObject.put(elements[0], elements[1]);
        }

        messages.put(new Message(MessageType.FIND, jsonObject.toString()));
    }

    private void showHelp() {
        System.out.println("HELP:");
        System.out.println("    - GET - fetch all decisions");
        System.out.println("    - GET <id> - fetch one decision by id");
        System.out.println("    - ADD - add decision");
        System.out.println("    - SEARCH <column>=<keyword> <column2>=<keyword2> ... - Search for a decision that satisfies all conditions");
        System.out.println("    - EXIT - shutdown application");
    }

    private boolean dateValid(String dateString) {
        Pattern datePattern = Pattern.compile("(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0,1,2])-(19|20)\\d{2}");
        Matcher matcher = datePattern.matcher(dateString);
        return matcher.matches() || dateString.isEmpty();
    }

    private boolean importanceValid(String importanceStr) {
        Set<String> validChoices = new HashSet<>(Arrays.asList("MINOR", "MAJOR", "CRITICAL"));

        if (Objects.equals(importanceStr, "")) {
            return true;
        }

        return validChoices.contains(importanceStr.toUpperCase());
    }
}
