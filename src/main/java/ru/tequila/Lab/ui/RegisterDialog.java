package ru.tequila.Lab.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.tequila.Lab.service.UserService;

public class RegisterDialog extends Dialog<Boolean> {
    public RegisterDialog(UserService userService) {
        setTitle("регистрация");
        setHeaderText("новый аккаунт лаборанта");

        ButtonType regButtonType = new ButtonType("зарегистрироваться", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(regButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        TextField username = new TextField();
        username.setPromptText("новый логин");
        PasswordField password = new PasswordField();
        password.setPromptText("новый пароль");

        grid.add(new Label("логин:"), 0, 0); grid.add(username, 1, 0);
        grid.add(new Label("пароль:"), 0, 1); grid.add(password, 1, 1);
        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == regButtonType) {
                boolean success = userService.register(username.getText().trim(), password.getText());
                if (success) {
                    new Alert(Alert.AlertType.INFORMATION, "ура! теперь войдите").showAndWait();
                    return true;
                } else {
                    new Alert(Alert.AlertType.ERROR, "логин занят или поля пустые").showAndWait();
                    return false;
                }
            }
            return null;
        });
    }
}