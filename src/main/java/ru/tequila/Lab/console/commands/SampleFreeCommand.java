package ru.tequila.Lab.console.commands;
import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.ContainerService;

public class SampleFreeCommand implements Command {
    private final ContainerService service;

    public SampleFreeCommand(ContainerService service) {
        this.service = service;
    }
    @Override
    public String name() {
        return "sample_free";
    }
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: введите ID бокса");
            return;
        }
        service.freeBox(Long.parseLong(args[1]));
    }
}