package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.service.ContainerService;

public class SampleFreeCommand {
    private final ContainerService service;

    public SampleFreeCommand(ContainerService service) {
        this.service = service;
    }

    public String name() {
        return "sample_free";
    }

    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: укажите ID бокса для освобождения");
            return;
        }
        try {
            long boxId = Long.parseLong(args[1]);
            System.out.println("Команда sample_free переведена в UI. Используйте кнопку освобождения в интерфейсе.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}