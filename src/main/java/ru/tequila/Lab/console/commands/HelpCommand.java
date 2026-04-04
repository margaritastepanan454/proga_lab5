package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.console.Command;

public class HelpCommand implements Command {
    @Override
    public String name() {
        return "help";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Список всякого разного");
        System.out.println("  container_add, container_list, container_show, container_update, container_status");
        System.out.println("  box_add, box_list, box_search");
        System.out.println("  sample_place, sample_free");
        System.out.println("  exit ");
    }
}