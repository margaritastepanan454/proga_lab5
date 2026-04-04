package ru.tequila.Lab.service;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.domain.ContainerStatus;
import ru.tequila.Lab.repository.InMemoryContainerRepository;
import ru.tequila.Lab.validator.ContainerValidator;

import java.time.Instant;
import java.util.List;
import java.util.Scanner;

public class ContainerService {

    private final InMemoryContainerRepository repo;
    private final Scanner scanner = new Scanner(System.in);

    public ContainerService(InMemoryContainerRepository repo) {
        this.repo = repo;
    }

    public void createContainer() {
        try {
            Container c = new Container();
            c.id = repo.nextContainerId();

            System.out.print("Название контейнера (напр. Freezer-1): ");
            c.name = scanner.nextLine().trim();

            System.out.print("Тип (freezer/rack/box): ");
            c.type = scanner.nextLine().trim();

            System.out.print("Местоположение: ");
            c.location = scanner.nextLine().trim();

            System.out.print("Вместимость (кол-во слотов) ");
            c.capacity = Integer.parseInt(scanner.nextLine().trim());

            c.status = ContainerStatus.ACTIVE;
            c.occupiedSlots = 0;
            c.ownerUsername = "SYSTEM";
            c.createdAt = Instant.now();
            c.updatedAt = c.createdAt;

            ContainerValidator.validateContainer(c);
            repo.saveContainer(c);

            System.out.println("Успешно создан контейнер. ID: " + c.id);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void listContainers() {
        List<Container> all = repo.findAllContainers();
        if (all.isEmpty()) {
            System.out.println("Контейнеров нит");
            return;
        }
        for (Container c : all) {
            System.out.println("ID: " + c.id + " | " + c.name + " (" + c.type + ") | Слот: " + c.occupiedSlots + "/" + c.capacity);
        }
    }

    public void createBox(long containerId) {
        Container container = repo.findContainerById(containerId);
        if (container == null) {
            System.out.println("Ошибка: Контейнер с ID " + containerId + " не найден.");
            return;
        }

        try {
            Box b = new Box();
            b.id = repo.nextBoxId();
            b.containerId = containerId;

            System.out.print("Название бокса: ");
            b.name = scanner.nextLine().trim();

            System.out.print("Номер слота: ");
            b.slotNumber = Integer.parseInt(scanner.nextLine().trim());

            b.isOccupied = false;
            b.ownerUsername = "SYSTEM";
            b.createdAt = Instant.now();

            ContainerValidator.validateBox(b);
            repo.saveBox(b);

            container.occupiedSlots++;

            System.out.println("Успешно создан бокс. ID: " + b.id);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }


    public void listBoxes(long containerId) {
        List<Box> boxes = repo.findBoxesByContainer(containerId);
        if (boxes.isEmpty()) {
            System.out.println("В этом контейнере пока нет боксов");
            return;
        }
        for (Box b : boxes) {
            String status = b.isOccupied ? "[ЗАНЯТ]" : "[СВОБОДЕН]";
            System.out.println("Box ID: " + b.id + " | Название: " + b.name + " | Слот №: " + b.slotNumber + " " + status);
        }
    }

    public void showContainer(long id) {
        Container c = repo.findContainerById(id);
        if (c == null) {
            System.out.println("Контейнер с ID " + id + " не найден.");
            return;
        }
        System.out.println(" Детали контейнера ");
        System.out.println("ID: " + c.id + " | Название: " + c.name);
        System.out.println("Тип: " + c.type + " | Локация: " + c.location);
        System.out.println("Статус: " + c.status + " | Занято: " + c.occupiedSlots + "/" + c.capacity);
    }


    public void updateContainer(long id) {
        Container c = repo.findContainerById(id);
        if (c == null) {
            System.out.println("Контейнер не найден.");
            return;
        }
        System.out.print("Новое название (было: " + c.name + "): ");
        c.name = scanner.nextLine().trim();
        System.out.print("Новая локация (была: " + c.location + "): ");
        c.location = scanner.nextLine().trim();
        c.updatedAt = Instant.now();
        System.out.println("Данные обновлены.");
    }


    public void changeStatus(long id) {
        Container c = repo.findContainerById(id);
        if (c == null) {
            System.out.println("Контейнер не найден");
            return;
        }
        System.out.print("Введите статус (ACTIVE, FULL, MAINTENANCE): ");
        try {
            String input = scanner.nextLine().trim().toUpperCase();
            c.status = ContainerStatus.valueOf(input);
            System.out.println("Статус обновлен");
        } catch (Exception e) {
            System.out.println("Ошибка: некорректный статус");
        }
    }


    public void searchBox(String name) {
        boolean found = false;
        for (Box b : repo.findAllBoxes()) {
            if (b.name.equalsIgnoreCase(name)) {
                System.out.println("Box найден: ID " + b.id + " в контейнере ID " + b.containerId);
                found = true;
            }
        }
        if (!found) System.out.println("Бокс '" + name + "' не найден.");
    }


    public void placeSample(long boxId) {
        Box b = repo.findBoxById(boxId);
        if (b == null) {
            System.out.println("Бокс не найден.");
            return;
        }
        if (b.isOccupied) {
            System.out.println("Бокс уже занят.");
            return;
        }
        b.isOccupied = true;
        b.occupiedSince = Instant.now();
        System.out.println("Образец помещен в бокс ID " + b.id);
    }


    public void freeBox(long boxId) {
        Box b = repo.findBoxById(boxId);
        if (b == null) {
            System.out.println("Бокс не найден.");
            return;
        }
        b.isOccupied = false;
        System.out.println("Бокс ID " + b.id + " теперь свободен.");
    }
}