package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.FileStorageService;

public class SaveCommand implements Command {
    private final FileStorageService storageService;

    public SaveCommand(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public String name() { return "save"; }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: Укажите имя файла. Пример: save database.ser");
            return;
        }
        storageService.saveToFile(args[1]);
    }
}