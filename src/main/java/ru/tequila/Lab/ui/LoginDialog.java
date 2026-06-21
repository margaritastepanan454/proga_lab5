package ru.tequila.Lab.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import ru.tequila.Lab.service.UserService;

public class LoginDialog extends Dialog<Pair<String, String>> {

    public LoginDialog(UserService userService) {
        setTitle("ОКАК");
        setHeaderText("как карта ляжет");

        ButtonType loginButtonType = new ButtonType("войти", ButtonBar.ButtonData.OK_DONE);
        ButtonType registerButtonType = new ButtonType("регистрация", ButtonBar.ButtonData.OTHER);

        getDialogPane().getButtonTypes().addAll(loginButtonType, registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));

        TextField username = new TextField(); username.setPromptText("логин");
        PasswordField password = new PasswordField(); password.setPromptText("пароль");

        grid.add(new Label("логин:"), 0, 0); grid.add(username, 1, 0);
        grid.add(new Label("пароль:"), 0, 1); grid.add(password, 1, 1);
        getDialogPane().setContent(grid);

        Node loginButton = getDialogPane().lookupButton(loginButtonType);
        loginButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (!userService.login(username.getText().trim(), password.getText())) {
                event.consume();
                new Alert(Alert.AlertType.ERROR, "неверный логин или пароль").showAndWait();
            }
        });

        Node regBtn = getDialogPane().lookupButton(registerButtonType);
        regBtn.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            event.consume();
            new RegisterDialog(userService).showAndWait();
        });

        setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) return new Pair<>(username.getText(), password.getText());
            return null;
        });
    }
}