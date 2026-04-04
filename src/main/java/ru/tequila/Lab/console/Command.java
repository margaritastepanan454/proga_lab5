package ru.tequila.Lab.console;

public interface Command {
    String name();
    void execute(String[] args);
}