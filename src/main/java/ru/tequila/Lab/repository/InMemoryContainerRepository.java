package ru.tequila.Lab.repository;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;
import java.util.*;

public class InMemoryContainerRepository {
    private final Map<Long, Container> containers = new HashMap<>();
    private final Map<Long, Box> boxes = new HashMap<>();

    private long nextContainerId = 1L;
    private long nextBoxId = 1L;

    public long nextContainerId() {
        return nextContainerId++;
    }
    public long nextBoxId() {
        return nextBoxId++;
    }

    public void saveContainer(Container c) {
        containers.put(c.id, c);
    }
    public void saveBox(Box b) {
        boxes.put(b.id, b);
    }

    public Container findContainerById(long id) {
        return containers.get(id);
    }
    public Box findBoxById(long id) {
        return boxes.get(id);
    }

    public List<Container> findAllContainers() {
        return new ArrayList<>(containers.values());
    }

    public List<Box> findAllBoxes() {
        return new ArrayList<>(boxes.values());
    }

    public List<Box> findBoxesByContainer(long containerId) {
        List<Box> result = new ArrayList<>();
        for (Box b : boxes.values()) {
            if (b.containerId == containerId) {
                result.add(b);
            }
        }
        return result;
    }
    public void deleteContainerById(long id) {
        containers.remove(id);
    }

    public void deleteBoxById(long id) {
        boxes.remove(id);
    }
}