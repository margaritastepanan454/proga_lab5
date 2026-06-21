package ru.tequila.Lab.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.tequila.Lab.domain.Box;
import java.time.Instant;

public class BoxDialog extends Dialog<Box> {

    public BoxDialog(long nextId, long containerId) {
        setTitle("новый бокс");
        setHeaderText("добавление бокса в контейнер ID " + containerId);

        ButtonType addButtonType = new ButtonType("добавить", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(15));

        TextField boxNameField = new TextField();
        boxNameField.setPromptText("Box-A");
        TextField slotField = new TextField("1");

        grid.add(new Label("название бокса:"), 0, 0); grid.add(boxNameField, 1, 0);
        grid.add(new Label("номер слота:"), 0, 1); grid.add(slotField, 1, 1);

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Box b = new Box();
                b.id = nextId;
                b.containerId = containerId;
                b.name = boxNameField.getText().trim();
                try {
                    b.slotNumber = Integer.parseInt(slotField.getText().trim());
                } catch (NumberFormatException e) {
                    b.slotNumber = 0;
                }
                b.isOccupied = false;
                b.ownerUsername = "SYSTEM";
                b.createdAt = Instant.now();
                return b;
            }
            return null;
        });
    }
}