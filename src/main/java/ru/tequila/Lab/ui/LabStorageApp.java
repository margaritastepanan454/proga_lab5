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
        // 1. Создаем единственный и общий экземпляр репозитория и сервисов для UI
        InMemoryContainerRepository repository = new InMemoryContainerRepository();
        ContainerService containerService = new ContainerService(repository);
        FileStorageService fileStorageService = new FileStorageService(repository);

        // 2. Инициализируем визуальный слой разметки (View)
        LabStorageView view = new LabStorageView();

        // 3. Создаем контроллер, который связывает View и Сервисы вместе
        new LabStorageController(view, repository, containerService, fileStorageService);

        // 4. Помещаем корень разметки в сцену и выводим окно на экран
        Scene scene = new Scene(view.getRoot(), 1024, 600);

        primaryStage.setTitle("Lab Storage Control System (Strict OOP MVC)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Метод launch запускает жизненный цикл JavaFX-приложения и вызывает метод start()
        launch(args);
    }
}