package ru.tequila.Lab.validator;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.domain.ContainerStatus;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

    public class FileValidator {

        public static void validateFileStructure(String filePath) throws ValidationException {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new ValidationException("Ошибка загрузки: Файл '" + filePath + "' не существует.");
            }
            if (!file.canRead()) {
                throw new ValidationException("Ошибка загрузки: Файл недоступен для чтения.");
            }
        }

        public static void validateLoadedData(Map<Long, Container> containers, Map<Long, Box> boxes) throws ValidationException {
            if (containers == null || boxes == null) {
                throw new ValidationException("Ошибка целостности: Структура файла нарушена (коллекции не могут быть null).");
            }

            for (Container c : containers.values()) {
                if (c.name == null || c.name.trim().isEmpty()) {
                    throw new ValidationException("Ошибка валидации данных: Обнаружен контейнер ID " + c.id + " с пустым именем.");
                }
                if (c.capacity < 1 || c.capacity > 1000) {
                    throw new ValidationException("Ошибка валидации данных: Контейнер ID " + c.id + " имеет некорректную вместимость.");
                }
                if (c.status == null) {
                    throw new ValidationException("Ошибка валидации данных: У контейнера ID " + c.id + " не указан статус.");
                }
            }

            Set<String> uniqueSlots = new HashSet<>();

            for (Box b : boxes.values()) {
                if (b.name == null || b.name.trim().isEmpty()) {
                    throw new ValidationException("Ошибка валидации данных: Бокс ID " + b.id + " имеет пустое имя.");
                }
                if (b.slotNumber < 1) {
                    throw new ValidationException("Ошибка валидации данных: Бокс ID " + b.id + " ссылается на некорректный слот.");
                }

                if (!containers.containsKey(b.containerId)) {
                    throw new ValidationException("Ошибка целостности данных: Бокс ID " + b.id +
                            " ссылается на несуществующий Контейнер ID " + b.containerId);
                }

                String slotKey = b.containerId + "_" + b.slotNumber;
                if (!uniqueSlots.add(slotKey)) {
                    throw new ValidationException("Ошибка дублирования: Слот №" + b.slotNumber +
                            " в контейнере ID " + b.containerId + " занят более чем одним боксом.");
                }
            }
        }
    }

