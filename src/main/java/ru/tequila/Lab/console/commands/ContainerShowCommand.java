package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.service.ContainerService;

public class ContainerShowCommand {
    private final ContainerService service;

    public ContainerShowCommand(ContainerService service) {
        this.service = service;
    }

    public String name() {
        return "container_show";
    }

    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: введите ID через пробел");
            return;
        }
        try {
            long id = Long.parseLong(args[1]);
            Container c = service.getContainer(id);
            if (c != null) {
                System.out.println("Контейнер найден: " + c.name + " [" + c.location + "], Тип: " + c.type + ", Статус: " + c.status);
            } else {
                System.out.println("Контейнер с ID " + id + " не найден.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}