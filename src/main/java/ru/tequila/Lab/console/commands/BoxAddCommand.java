package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.service.ContainerService;

public class BoxAddCommand {
    private final ContainerService service;

    public BoxAddCommand(ContainerService service) {
        this.service = service;
    }

    public String name() {
        return "box_add";
    }

    public void execute(String[] args) {
        if (args.length < 4) {
            System.out.println("Ошибка. Формат: box_add <имя_бокса> <id_контейнера> <номер_слота>");
            return;
        }
        try {
            Box b = new Box();
            b.name = args[1];
            b.containerId = Long.parseLong(args[2]);
            b.slotNumber = Integer.parseInt(args[3]);
            b.isOccupied = false;

            service.createBox(b);
            System.out.println("Бокс успешно добавлен с ID: " + b.id);
        } catch (Exception e) {
            System.out.println("Ошибка добавления бокса: " + e.getMessage());
        }
    }
}