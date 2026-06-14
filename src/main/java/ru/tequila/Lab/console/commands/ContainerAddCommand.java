package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.domain.ContainerStatus;
import ru.tequila.Lab.service.ContainerService;

public class ContainerAddCommand {
    private final ContainerService service;

    public ContainerAddCommand(ContainerService service) {
        this.service = service;
    }

    public String name() {
        return "container_add";
    }

    public void execute(String[] args) {
        if (args.length < 5) {
            System.out.println("Ошибка. Формат: container_add <имя> <тип> <локация> <вместимость>");
            return;
        }
        try {
            Container c = new Container();
            c.name = args[1];
            c.type = args[2];
            c.location = args[3];
            c.capacity = Integer.parseInt(args[4]);
            c.status = ContainerStatus.ACTIVE; // Дефолтный статус из твоего Enum
            c.occupiedSlots = 0;

            service.createContainer(c);
            System.out.println("Контейнер успешно создан в БД с ID: " + c.id);
        } catch (Exception e) {
            System.out.println("Ошибка создания контейнера: " + e.getMessage());
        }
    }
}