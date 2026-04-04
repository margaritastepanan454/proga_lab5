package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.ContainerService;

public class ContainerAddCommand implements Command {
    private final ContainerService service;

    public ContainerAddCommand(ContainerService service) {
        this.service = service;
    }
    @Override
    public String name() {
        return "container_add";
    }
    @Override
    public void execute(String[] args) {
        service.createContainer();
    }
}