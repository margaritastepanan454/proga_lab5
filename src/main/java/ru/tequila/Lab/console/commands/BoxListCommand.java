package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.service.ContainerService;
import java.util.List;

public class BoxListCommand {
    private final ContainerService service;

    public BoxListCommand(ContainerService service) {
        this.service = service;
    }

    public String name() {
        return "box_list";
    }

    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: укажите ID контейнера");
            return;
        }
        try {
            long containerId = Long.parseLong(args[1]);
            List<Box> boxes = service.getBoxesByContainer(containerId);
            if (boxes.isEmpty()) {
                System.out.println("В контейнере с ID " + containerId + " нет боксов.");
            } else {
                System.out.println("Список боксов в контейнере " + containerId + ":");
                for (Box b : boxes) {
                    System.out.println(String.format("  - ID: %d | Название: %s | Слот: %d | Занят: %s",
                            b.id, b.name, b.slotNumber, b.isOccupied ? "Да" : "Нет"));
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}