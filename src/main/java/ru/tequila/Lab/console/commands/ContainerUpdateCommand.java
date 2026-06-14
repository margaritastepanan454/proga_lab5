package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.service.ContainerService;

public class ContainerUpdateCommand {
    private final ContainerService service;

    public ContainerUpdateCommand(ContainerService service) {
        this.service = service;
    }

    public String name() {
        return "container_update";
    }

    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: недостаточно аргументов.");
            return;
        }
        try {
            long id = Long.parseLong(args[1]);
            Container c = service.getContainer(id);
            if (c != null) {
                // Если в аргументах передано новое имя, обновляем его
                if (args.length > 2) {
                    c.name = args[2];
                }
                service.createContainer(c); // saveContainer внутри сервиса выполнит UPDATE для существующего ID
                System.out.println("Контейнер успешно обновлен.");
            } else {
                System.out.println("Контейнер с ID " + id + " не найден.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении контейнера: " + e.getMessage());
        }
    }
}