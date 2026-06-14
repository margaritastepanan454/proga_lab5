package ru.tequila.Lab.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.tequila.Lab.repository.ContainerRepository;
import ru.tequila.Lab.repository.JdbcContainerRepository;
import ru.tequila.Lab.service.ContainerService;
import ru.tequila.Lab.service.HistoryService;

public class LabStorageApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        ContainerRepository repository = new JdbcContainerRepository();
        ContainerService containerService = new ContainerService(repository);
        HistoryService historyService = HistoryService.getInstance();
        LabStorageView view = new LabStorageView();

        new LabStorageController(view, repository, containerService, historyService);

        Scene scene = new Scene(view.getRoot(), 1024, 600);
        if (getClass().getResource("/style.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        }

        primaryStage.setTitle("Lab Storage Control System (Strict OOP MVC & JDBC)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}