package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.ContainerService;

public class BoxListCommand implements Command {
    private final ContainerService service;
    public BoxListCommand(ContainerService service) {
        this.service = service;
    }
    @Override
    public String name() {
        return "box_list";
    }
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: укажите ID контейнера, чтобы увидеть его коробки box_list 1)");
            return;
        }
        service.listBoxes(Long.parseLong(args[1]));
    }
}