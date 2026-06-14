package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.service.ContainerService;

public class DeleteCommand {
    private final ContainerService service;

    public DeleteCommand(ContainerService service) {
        this.service = service;
    }

    public String name() {
        return "delete";
    }

    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Ошибка: используйте 'delete container <id>' или 'delete box <id>'");
            return;
        }
        try {
            String type = args[1].toLowerCase();
            long id = Long.parseLong(args[2]);

            if ("container".equals(type)) {
                service.removeContainer(id);
                System.out.println("Контейнер удален.");
            } else if ("box".equals(type)) {
                service.removeBox(id);
                System.out.println("Бокс удален.");
            } else {
                System.out.println("Неизвестный тип для удаления: " + type);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении: " + e.getMessage());
        }
    }
}