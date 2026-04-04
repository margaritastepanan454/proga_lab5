package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.ContainerService;

public class BoxSearchCommand implements Command {
    private final ContainerService service;

    public BoxSearchCommand(ContainerService service) {
        this.service = service;
    }
    @Override
    public String name() {
        return "box_search";
    }
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: введите имя для поиска");
            return;
        }
        service.searchBox(args[1]);
    }
}