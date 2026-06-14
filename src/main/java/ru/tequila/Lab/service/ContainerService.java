package ru.tequila.Lab.service;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.repository.ContainerRepository;
import java.util.List;

public class ContainerService {
    private final ContainerRepository repo;

    public ContainerService(ContainerRepository repo) {
        this.repo = repo;
    }

    public void createContainer(Container c) {
        repo.saveContainer(c);
    }

    public void createBox(Box b) {
        repo.saveBox(b);
    }

    public Container getContainer(long id) {
        return repo.findContainerById(id);
    }

    public Box getBox(long id) {
        return repo.findBoxById(id);
    }

    public List<Container> getAllContainers() {
        return repo.findAllContainers();
    }

    public List<Box> getBoxesByContainer(long containerId) {
        return repo.findBoxesByContainer(containerId);
    }

    public void removeContainer(long id) {
        repo.deleteContainerById(id);
    }

    public void removeBox(long id) {
        repo.deleteBoxById(id);
    }
}