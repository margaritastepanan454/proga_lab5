package ru.tequila.Lab;

import ru.tequila.Lab.console.CommandProcessor;

public class Main {
    public static void main(String[] args) {
        System.out.println("Lab Storage Control System v1.0");
        System.out.println("скажите богу 'help' и он ответит вам");
        CommandProcessor processor = new CommandProcessor();
        processor.run();
    }
}