package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.ContainerService;

public class ContainerStatusCommand implements Command {
    private final ContainerService service;

    public ContainerStatusCommand(ContainerService service) {
        this.service = service;
    }
    @Override
    public String name() {
        return "container_status";
    }
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: введите ID");
            return;
        }
        service.changeStatus(Long.parseLong(args[1]));
    }
}