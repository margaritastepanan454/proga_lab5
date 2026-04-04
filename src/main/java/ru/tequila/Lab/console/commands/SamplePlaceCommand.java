package ru.tequila.Lab.console.commands;
import ru.tequila.Lab.console.Command;
import ru.tequila.Lab.service.ContainerService;

public class SamplePlaceCommand implements Command {
    private final ContainerService service;

    public SamplePlaceCommand(ContainerService service) {
        this.service = service;
    }
    @Override
    public String name() {
        return "sample_place";
    }
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: введите ID бокса");
            return;
        }
        service.placeSample(Long.parseLong(args[1]));
    }
}