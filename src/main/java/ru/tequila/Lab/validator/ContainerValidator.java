package ru.tequila.Lab.validator;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;

public class ContainerValidator {

    public static void validateContainer(Container container) throws ValidationException {
        if (container.name == null || container.name.trim().isEmpty()) {
            throw new ValidationException("Ошибка: название не может быть пустым");
        }
        if (container.name.length() > 128) {
            throw new ValidationException("Ошибка: название слишком длинное (макс. 128)");
        }
        if (container.type == null || container.type.trim().isEmpty()) {
            throw new ValidationException("Ошибка: тип не может быть пустым");
        }
        if (!isValidType(container.type)) {
            throw new ValidationException("Ошибка: тип должен быть freezer, rack или box");
        }
        if (container.location == null || container.location.trim().isEmpty()) {
            throw new ValidationException("Ошибка: location не может быть пустым");
        }
        if (container.capacity < 1 || container.capacity > 1000) {
            throw new ValidationException("Ошибка: вместимость должна быть от 1 до 1000");
        }
        if (container.occupiedSlots < 0 || container.occupiedSlots > container.capacity) {
            throw new ValidationException("Ошибка: occupiedSlots некорректно");
        }
    }

    public static void validateBox(Box box) throws ValidationException {
        if (box.containerId <= 0) {
            throw new ValidationException("Ошибка: containerId должен быть положительным");
        }
        if (box.name == null || box.name.trim().isEmpty()) {
            throw new ValidationException("Ошибка: название бокса не может быть пустым");
        }
        if (box.slotNumber < 1) {
            throw new ValidationException("Ошибка: номер слота должен быть >= 1");
        }
    }

    private static boolean isValidType(String type) {
        String t = type.toLowerCase();
        return t.equals("freezer") || t.equals("rack") || t.equals("box");
    }
}