package ru.tequila.Lab.repository;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryContainerRepository implements ContainerRepository {
    private final Map<Long, Container> containers = new HashMap<>();
    private final Map<Long, Box> boxes = new HashMap<>();
    private long containerIdSequence = 1;
    private long boxIdSequence = 1;

    @Override
    public void saveContainer(Container c) {
        if (c.id <= 0) {
            c.id = containerIdSequence++;
        }
        containers.put(c.id, c);
    }

    @Override
    public void saveBox(Box b) {
        if (b.id <= 0) {
            b.id = boxIdSequence++;
        }
        boxes.put(b.id, b);
        updateOccupiedSlots(b.containerId);
    }

    @Override
    public Container findContainerById(long id) {
        return containers.get(id);
    }

    @Override
    public Box findBoxById(long id) {
        return boxes.get(id);
    }

    @Override
    public List<Container> findAllContainers() {
        return new ArrayList<>(containers.values());
    }

    @Override
    public List<Box> findAllBoxes() {
        return new ArrayList<>(boxes.values());
    }

    @Override
    public List<Box> findBoxesByContainer(long containerId) {
        return boxes.values().stream()
                .filter(b -> b.containerId == containerId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteContainerById(long id) {
        containers.remove(id);
        boxes.values().removeIf(b -> b.containerId == id);
    }

    @Override
    public void deleteBoxById(long id) {
        Box b = boxes.remove(id);
        if (b != null) {
            updateOccupiedSlots(b.containerId);
        }
    }

    private void updateOccupiedSlots(long containerId) {
        Container c = containers.get(containerId);
        if (c != null) {
            long count = boxes.values().stream()
                    .filter(b -> b.containerId == containerId)
                    .count();
            c.occupiedSlots = (int) count;
        }
    }
}