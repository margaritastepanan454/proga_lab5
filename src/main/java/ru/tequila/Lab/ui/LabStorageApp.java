package ru.tequila.Lab.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.tequila.Lab.repository.InMemoryContainerRepository;
import ru.tequila.Lab.service.ContainerService;
import ru.tequila.Lab.service.FileStorageService;

public class LabStorageApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        InMemoryContainerRepository repository = new InMemoryContainerRepository();
        ContainerService containerService = new ContainerService(repository);
        FileStorageService fileStorageService = new FileStorageService(repository);

        LabStorageView view = new LabStorageView();

        new LabStorageController(view, repository, containerService, fileStorageService);

        Scene scene = new Scene(view.getRoot(), 1024, 600);

        primaryStage.setTitle("Lab Storage Control System (Strict OOP MVC)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}