package ru.tequila.Lab.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HistoryService {
    private static HistoryService instance;

    private final LinkedList<String> commandHistory = new LinkedList<>();
    private final int MAX_HISTORY_SIZE = 10;

    private HistoryService() {}

    public static synchronized HistoryService getInstance() {
        if (instance == null) {
            instance = new HistoryService();
        }
        return instance;
    }

    public void addCommand(String commandDescription) {
        if (commandDescription == null || commandDescription.isBlank()) {
            return;
        }

        commandHistory.addFirst(commandDescription);

        if (commandHistory.size() > MAX_HISTORY_SIZE) {
            commandHistory.removeLast();
        }
    }

    public List<String> getHistory() {
        return new ArrayList<>(commandHistory);
    }
}