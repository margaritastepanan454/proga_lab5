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
        System.out.println(" box_add - добавить новую коробку для хранения посуды ");
        System.out.println(" container_add - зарегистрировать новый контейнер в системе");
        System.out.println(" box_list - вывести список всех существующих коробок");
        System.out.println(" container_List - показать список всей лабораторной посуды");
        System.out.println(" container_Show - посмотреть подробную информацию о контейнере");
        System.out.println(" container_update - изменить параметры или данные контейнера");
        System.out.println(" box_search - найти конкретную коробку по её названию");
        System.out.println(" sample_place - поместить образец/вещество в контейнер");
        System.out.println(" sample_free - освободить контейнер от образца");
        System.out.println(" container_Status - проверить текущий статус контейнера свободен/занят");
        System.out.println(" delete - удалить сущность по ID (содержимое контейнеров сохраняется)");
        System.out.println(" exit ");
    }
}