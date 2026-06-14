package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.service.ContainerService;

public class SamplePlaceCommand {
    public SamplePlaceCommand(ContainerService service) {
    }

    public String name() {
        return "sample_place";
    }

    public void execute(String[] args) {
        System.out.println("Управление занятостью боксов теперь выполняется через графический интерфейс.");
    }
}