package ru.tequila.Lab;

import ru.tequila.Lab.console.CommandProcessor;
import ru.tequila.Lab.repository.InMemoryContainerRepository;
import ru.tequila.Lab.service.ContainerService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Lab Storage Control System v1.0");
        System.out.println("Скажите богу 'help' и он ответит вам");

        // 1. Создаем необходимые зависимости (слой данных и слой логики)
        InMemoryContainerRepository repo = new InMemoryContainerRepository();
        ContainerService s = new ContainerService(repo);

        // 2. Передаем их в конструктор CommandProcessor, как требует твой код
        CommandProcessor processor = new CommandProcessor(repo, s);

        // 3. Проверяем аргументы запуска (автозагрузка файла)
        if (args.length > 0) {
            System.out.println("Обнаружен аргумент запуска. Попытка загрузки базы данных...");
            processor.execute(new String[]{"load", args[0]});
        }

        // 4. Бесконечный цикл чтения консоли
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