package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.ContainerService;

public class ContainerListCommand implements Command {

    private final ContainerService service;
    public ContainerListCommand(ContainerService service) {
        this.service = service;
    }
    @Override
    public String name() {
        return "container_List";
    }
    @Override
    public void execute(String[] args) {
        service.listContainers();
    }
}