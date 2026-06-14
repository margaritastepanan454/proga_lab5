package ru.tequila.Lab.console.commands;

import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.domain.ContainerStatus;
import ru.tequila.Lab.service.ContainerService;

public class ContainerStatusCommand {
    private final ContainerService service;

    public ContainerStatusCommand(ContainerService service) {
        this.service = service;
    }

    public String name() {
        return "container_status";
    }

    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: введите ID");
            return;
        }
        try {
            long id = Long.parseLong(args[1]);
            Container c = service.getContainer(id);
            if (c == null) {
                System.out.println("Контейнер с ID " + id + " не найден.");
                return;
            }
            if (args.length > 2) {
                c.status = ContainerStatus.valueOf(args[2].toUpperCase());
                service.createContainer(c);
                System.out.println("Статус контейнера успешно обновлен на: " + c.status);
            } else {
                System.out.println("Текущий статус контейнера: " + c.status);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный статус. Допустимые значения: ACTIVE, ICE, FIRE (или ваши значения из ContainerStatus)");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}