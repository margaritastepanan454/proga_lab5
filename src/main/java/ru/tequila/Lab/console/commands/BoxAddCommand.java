package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.ContainerService;

public class BoxAddCommand implements Command {
    private final ContainerService service;

    public BoxAddCommand(ContainerService service) {
        this.service = service;
    }
    @Override
    public String name() {
        return "box_add";
    }
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: введите ID контейнера через пробел");
            return;
        }
        service.createBox(Long.parseLong(args[1]));
    }
}