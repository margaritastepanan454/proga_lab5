package ru.tequila.Lab.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HistoryService {
    // Единственный экземпляр класса (Singleton)
    private static HistoryService instance;

    private final LinkedList<String> commandHistory = new LinkedList<>();
    private final int MAX_HISTORY_SIZE = 10;

    // Приватный конструктор: теперь нельзя вызвать 'new HistoryService()' снаружи
    private HistoryService() {}

    // Глобальная точка доступа к синглтону
    public static synchronized HistoryService getInstance() {
        if (instance == null) {
            instance = new HistoryService();
        }
        return instance;
    }

    // Метод для добавления команды в историю
    public void addCommand(String commandDescription) {
        if (commandDescription == null || commandDescription.isBlank()) {
            return;
        }

        commandHistory.addFirst(commandDescription);

        if (commandHistory.size() > MAX_HISTORY_SIZE) {
            commandHistory.removeLast();
        }
    }

    // Метод для получения всей текущей истории
    public List<String> getHistory() {
        return new ArrayList<>(commandHistory);
    }
}