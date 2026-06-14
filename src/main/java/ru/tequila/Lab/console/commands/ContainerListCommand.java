package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.service.ContainerService;
import java.util.List;

public class ContainerListCommand {
    private final ContainerService service;

    public ContainerListCommand(ContainerService service) {
        this.service = service;
    }

    public String name() {
        return "container_list";
    }

    public void execute(String[] args) {
        try {
            List<Container> containers = service.getAllContainers();
            if (containers.isEmpty()) {
                System.out.println("Список контейнеров пуст.");
            } else {
                for (Container c : containers) {
                    System.out.println(String.format("ID: %d | Имя: %s | Тип: %s | Статус: %s | Занято слотов: %d/%d",
                            c.id, c.name, c.type, c.status, c.occupiedSlots, c.capacity));
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка контейнеров: " + e.getMessage());
        }
    }
}