package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.ContainerService;

public class DeleteCommand implements Command {
    private final ContainerService containerService;

    public DeleteCommand(ContainerService containerService) {
        this.containerService = containerService;
    }

    @Override
    public String name() {
        return "delete";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Ошибка! Неверный формат команды.");
            System.out.println("Используйте: delete [container / box / sample] [ID]");
            return;
        }

        String entityType = args[1].toLowerCase();
        long id;
        try {
            id = Long.parseLong(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID сущности должен быть целым числом!");
            return;
        }

        switch (entityType) {
            case "container":
                containerService.deleteContainer(id);
                break;

            case "box":
                containerService.deleteBox(id);
                break;

            case "sample":
                System.out.println("Вызвана логика удаления образца с ID " + id);
                break;

            default:
                System.out.println("Ошибка: неизвестный тип сущности '" + entityType + "'");
                System.out.println("Допустимые типы: container, box, sample");
                break;
        }
    }
}