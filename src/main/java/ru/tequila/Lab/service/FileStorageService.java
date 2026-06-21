package ru.tequila.Lab.service;

import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.domain.ContainerStatus;
import java.io.*;
import java.util.List;

public class FileStorageService {
    private final ContainerService containerService;

    public FileStorageService(ContainerService containerService) {
        this.containerService = containerService;
    }

    public void saveToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            List<Container> containers = containerService.getAllContainers();
            for (Container c : containers) {
                writer.println(String.format("CONTAINER;%s;%s;%s;%d;%s;%d",
                        c.name, c.type, c.location, c.capacity, c.status.name(), c.occupiedSlots));

                List<Box> boxes = containerService.getBoxesByContainer(c.id);
                for (Box b : boxes) {
                    writer.println(String.format("BOX;%s;%d;%b",
                            b.name, b.slotNumber, b.isOccupied));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            long currentContainerId = -1;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                if ("CONTAINER".equals(tokens[0])) {
                    Container c = new Container();
                    c.name = tokens[1];
                    c.type = tokens[2];
                    c.location = tokens[3];
                    c.capacity = Integer.parseInt(tokens[4]);
                    c.status = ContainerStatus.valueOf(tokens[5]);
                    c.occupiedSlots = Integer.parseInt(tokens[6]);
                    containerService.createContainer(c);
                    currentContainerId = c.id;
                }
                else if ("BOX".equals(tokens[0]) && currentContainerId != -1) {
                    Box b = new Box();
                    b.name = tokens[1];
                    b.containerId = currentContainerId;
                    b.slotNumber = Integer.parseInt(tokens[2]);
                    b.isOccupied = Boolean.parseBoolean(tokens[3]);
                    containerService.createBox(b);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}