package ru.tequila.Lab.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.tequila.Lab.repository.JdbcContainerRepository;
import ru.tequila.Lab.repository.UserRepository;
import ru.tequila.Lab.service.UserService;
import java.sql.Connection;
import java.sql.DriverManager;

public class LabStorageApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:lab_storage.db");
        UserRepository userRepo = new UserRepository(conn);
        JdbcContainerRepository dataRepo = new JdbcContainerRepository();
        UserService userService = new UserService(userRepo);

        userService.register("admin", "admin");

        LoginDialog loginDialog = new LoginDialog(userService);
        var result = loginDialog.showAndWait();

        if (result.isPresent()) {
            String user = result.get().getKey();
            LabStorageView view = new LabStorageView();
            new LabStorageController(view, dataRepo, user);

            Scene scene = new Scene(view.getRoot(), 1100, 750);
            try {
                scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            } catch (Exception e) {}

            stage.setScene(scene);
            stage.setTitle("Lab Storage | " + user);
            stage.show();
        } else { System.exit(0); }
    }

    public static void main(String[] args) { launch(args); }
}