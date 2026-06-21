package ru.tequila.Lab.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.domain.ContainerStatus;
import java.time.Instant;

public class ContainerDialog extends Dialog<Container> {

    public ContainerDialog(long nextId) {
        setTitle("новый контейнер");
        setHeaderText("введите параметры контейнера:");

        ButtonType createButtonType = new ButtonType("создать", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(15));

        // Поля ввода
        TextField nameField = new TextField(); nameField.setPromptText("пример, Freezer-1)");
        TextField typeField = new TextField(); typeField.setPromptText("тип freezer/rack/box");
        TextField locationField = new TextField(); locationField.setPromptText("локация");
        TextField capacityField = new TextField("10");

        grid.add(new Label("название:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("тип:"), 0, 1); grid.add(typeField, 1, 1);
        grid.add(new Label("локация:"), 0, 2); grid.add(locationField, 1, 2);
        grid.add(new Label("вместимость:"), 0, 3); grid.add(capacityField, 1, 3);

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                Container c = new Container();
                c.name = nameField.getText().trim();
                c.type = typeField.getText().trim();
                c.location = locationField.getText().trim();
                try {
                    c.capacity = Integer.parseInt(capacityField.getText().trim());
                } catch (NumberFormatException e) {
                    c.capacity = 1;
                }
                c.status = ContainerStatus.ACTIVE; // По умолчанию
                c.createdAt = Instant.now();
                return c;
            }
            return null;
        });
    }
}