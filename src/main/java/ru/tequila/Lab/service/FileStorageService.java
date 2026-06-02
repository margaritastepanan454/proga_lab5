package ru.tequila.Lab.service; // Если файл лежит в папке storage, замени service на storage

import ru.tequila.Lab.repository.InMemoryContainerRepository;
import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.domain.Box;

import java.io.*;
import java.util.Map;

public class FileStorageService {
    private final InMemoryContainerRepository repository;

    public FileStorageService(InMemoryContainerRepository repository) {
        this.repository = repository;
    }

    public void saveToFile(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(repository.getContainerMap());
            oos.writeObject(repository.getBoxMap());
            System.out.println("Данные успешно сохранены в файл: " + filePath);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Ошибка: Файл '" + filePath + "' не найден!");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Map<Long, Container> loadedContainers = (Map<Long, Container>) ois.readObject();
            Map<Long, Box> loadedBoxes = (Map<Long, Box>) ois.readObject();
            repository.replaceData(loadedContainers, loadedBoxes);

            System.out.println("Данные успешно загружены из файла: " + filePath);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке файла: " + e.getMessage());
        }
    }
}