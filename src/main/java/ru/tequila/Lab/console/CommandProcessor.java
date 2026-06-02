package ru.tequila.Lab.console;

import ru.tequila.Lab.repository.InMemoryContainerRepository;
import ru.tequila.Lab.service.ContainerService;
import ru.tequila.Lab.service.FileStorageService;
import ru.tequila.Lab.validator.FileValidator;

import java.util.Arrays;

public class CommandProcessor {
    private final ContainerService containerService;
    private final FileStorageService fileStorageService;

    public CommandProcessor(InMemoryContainerRepository repo, ContainerService containerService) {
        this.containerService = containerService;
        this.fileStorageService = new FileStorageService(repo);
    }

    public void execute(String[] args) {
        if (args == null || args.length == 0) return;

        String command = args[0].toLowerCase();

        try {
            switch (command) {
                case "help":
                    printHelp();
                    break;
                case "save":
                    if (args.length < 2) {
                        System.out.println("Ошибка: Укажите путь к файлу. Пример: save storage.dat");
                        break;
                    }
                    fileStorageService.saveToFile(args[1]);
                    break;
                case "load":
                    if (args.length < 2) {
                        System.out.println("Ошибка: Укажите путь к файлу. Пример: load storage.dat");
                        break;
                    }
                    FileValidator.validateFileStructure(args[1]);
                    fileStorageService.loadFromFile(args[1]);
                    break;
                case "create-container":
                    containerService.createContainer();
                    break;
                case "list-containers":
                    containerService.listContainers();
                    break;
                case "delete-container":
                    if (args.length < 2) {
                        System.out.println("Ошибка: Введите ID контейнера");
                        break;
                    }
                    containerService.deleteContainer(Long.parseLong(args[1]));
                    break;
                default:
                    System.out.println("Неизвестная команда. Введите 'help' для списка доступных команд.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении команды: " + e.getMessage());
        }
    }

    private void printHelp() {
        System.out.println("=== Доступные команды ===");
        System.out.println("save <path>             - Сохранить базу данных в файл");
        System.out.println("load <path>             - Загрузить базу данных из файла");
        System.out.println("create-container        - Создать новый контейнер");
        System.out.println("list-containers         - Показать все контейнеры");
        System.out.println("delete-container <id>   - Каскадное удаление контейнера");
        System.out.println("exit                    - Выход из программы");
    }
}