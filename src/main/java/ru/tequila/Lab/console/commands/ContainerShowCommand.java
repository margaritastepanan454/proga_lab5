package ru.tequila.Lab.console.commands;
import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.ContainerService;

public class ContainerShowCommand implements Command {
    private final ContainerService service;
    public ContainerShowCommand(ContainerService service) {
        this.service = service;
    }

    @Override
    public String name() {
        return "container_show";
    }
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: введите ID");
            return;
        }
        service.showContainer(Long.parseLong(args[1]));
    }
}