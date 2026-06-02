package ru.tequila.Lab;

import ru.tequila.Lab.console.CommandProcessor;
import ru.tequila.Lab.repository.InMemoryContainerRepository;
import ru.tequila.Lab.service.ContainerService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Lab Storage Control System v1.0");
        System.out.println("Скажите богу 'help' и он ответит вам");

        InMemoryContainerRepository repo = new InMemoryContainerRepository();
        ContainerService s = new ContainerService(repo);

        CommandProcessor processor = new CommandProcessor(repo, s);

        if (args.length > 0) {
            System.out.println("Обнаружен аргумент запуска. Попытка загрузки базы данных...");
            processor.execute(new String[]{"load", args[0]});
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Завершение работы программы. До свидания!");
                break;
            }

            String[] commandArgs = line.split("\\s+");
            processor.execute(commandArgs);
        }
    }
}