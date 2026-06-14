package ru.tequila.Lab.repository;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;
import java.util.List;

public interface ContainerRepository {
    void saveContainer(Container c);
    void saveBox(Box b);
    Container findContainerById(long id);
    Box findBoxById(long id);
    List<Container> findAllContainers();
    List<Box> findAllBoxes();
    List<Box> findBoxesByContainer(long containerId);
    void deleteContainerById(long id);
    void deleteBoxById(long id);
}