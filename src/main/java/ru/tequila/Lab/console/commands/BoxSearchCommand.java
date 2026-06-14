package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.service.ContainerService;
import java.util.List;

public class BoxSearchCommand {
    private final ContainerService service;

    public BoxSearchCommand(ContainerService service) {
        this.service = service;
    }

    public String name() {
        return "box_search";
    }

    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: укажите ID контейнера для поиска боксов.");
            return;
        }
        try {
            long containerId = Long.parseLong(args[1]);
            List<Box> boxes = service.getBoxesByContainer(containerId);
            if (boxes.isEmpty()) {
                System.out.println("Боксы в контейнере не найдены.");
            } else {
                for (Box b : boxes) {
                    System.out.println("Бокс: ID=" + b.id + ", Название=" + b.name + ", Занят=" + b.isOccupied);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при поиске боксов: " + e.getMessage());
        }
    }
}