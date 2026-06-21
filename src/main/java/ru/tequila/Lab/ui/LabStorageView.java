package ru.tequila.Lab.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import ru.tequila.Lab.domain.*;

public class LabStorageView {
    private final BorderPane root = new BorderPane();
    public TableView<Container> containerTable = new TableView<>();
    public TableView<Box> boxTable = new TableView<>();
    public ListView<String> historyList = new ListView<>();

    public Button btnAddContainer = new Button("контейнер");
    public Button btnStatus = new Button("статус");
    public Button btnAddBox = new Button("бокс");
    public Button btnBoxStatus = new Button("занять");
    public Button btnDelete = new Button("удалить");
    public Button btnLogout = new Button("выход");

    public LabStorageView() {
        root.getStyleClass().add("root");

        HBox tools = new HBox(10, btnAddContainer, btnStatus, new Separator(),
                btnAddBox, btnBoxStatus, new Separator(),
                btnDelete, new Separator(), btnLogout);
        tools.setPadding(new Insets(15));
        tools.getStyleClass().add("vbox");

        setupContainerTable();
        setupBoxTable();

        VBox containerWrap = new VBox(10, new Label("список контейнеров"), containerTable);
        containerWrap.setPadding(new Insets(10, 20, 10, 20));

        VBox boxWrap = new VBox(10, new Label("содержимое выбранного контейнера"), boxTable);
        boxWrap.setPadding(new Insets(10, 20, 10, 20));

        SplitPane tablesSplit = new SplitPane(containerWrap, boxWrap);
        tablesSplit.setOrientation(javafx.geometry.Orientation.VERTICAL);

        VBox historyPane = new VBox(10, new Label("история"), historyList);
        historyPane.setPadding(new Insets(10));
        historyPane.setPrefWidth(250);

        root.setTop(tools);
        root.setCenter(tablesSplit);
        root.setRight(historyPane);
    }

    private void setupContainerTable() {
        TableColumn<Container, String> nameCol = new TableColumn<>("контейнер");
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().name));
        TableColumn<Container, String> stCol = new TableColumn<>("статус");
        stCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().status.name()));
        TableColumn<Container, String> ownCol = new TableColumn<>("владелец");
        ownCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().ownerUsername));
        containerTable.getColumns().addAll(nameCol, stCol, ownCol);
        containerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupBoxTable() {
        TableColumn<Box, String> nameCol = new TableColumn<>("бокс");
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().name));
        TableColumn<Box, Integer> slotCol = new TableColumn<>("слот");
        slotCol.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().slotNumber));
        TableColumn<Box, String> occCol = new TableColumn<>("занят");
        occCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isOccupied ? "ДА" : "НЕТ"));
        boxTable.getColumns().addAll(nameCol, slotCol, occCol);
        boxTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public BorderPane getRoot() {
        return root;
    }
}