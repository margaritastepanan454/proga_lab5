package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.FileStorageService;

public class LoadCommand implements Command {
    private final FileStorageService storageService;

    public LoadCommand(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public String name() { return "load"; }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: Укажите имя файла. Пример: load database.ser");
            return;
        }
        storageService.loadFromFile(args[1]);
    }
}