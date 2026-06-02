package ru.tequila.Lab.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.domain.ContainerStatus;
import java.time.Instant;

public class ContainerDialog extends Dialog<Container> {

    public ContainerDialog(long nextId) {
        setTitle("Новый контейнер");
        setHeaderText("Введите параметры лабораторного контейнера:");

        ButtonType createButtonType = new ButtonType("Создать", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(15));

        TextField nameField = new TextField();
        nameField.setPromptText("Freezer-1");
        TextField typeField = new TextField();
        typeField.setPromptText("freezer/rack/box");
        TextField locationField = new TextField();
        locationField.setPromptText("Стеллаж А");
        TextField capacityField = new TextField("10");

        grid.add(new Label("Название:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Тип:"), 0, 1); grid.add(typeField, 1, 1);
        grid.add(new Label("Локация:"), 0, 2); grid.add(locationField, 1, 2);
        grid.add(new Label("Вместимость:"), 0, 3); grid.add(capacityField, 1, 3);

        getDialogPane().setContent(grid);

        // Преобразуем нажатие кнопки "Создать" в готовый объект модели
        setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                Container c = new Container();
                c.id = nextId;
                c.name = nameField.getText().trim();
                c.type = typeField.getText().trim();
                c.location = locationField.getText().trim();
                try {
                    c.capacity = Integer.parseInt(capacityField.getText().trim());
                } catch (NumberFormatException e) {
                    c.capacity = 0; // Валидатор поймает это значение
                }
                c.status = ContainerStatus.ACTIVE;
                c.occupiedSlots = 0;
                c.ownerUsername = "SYSTEM";
                c.createdAt = Instant.now();
                c.updatedAt = c.createdAt;
                return c;
            }
            return null;
        });
    }
}