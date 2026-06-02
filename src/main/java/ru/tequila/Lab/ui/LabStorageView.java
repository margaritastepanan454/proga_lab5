package ru.tequila.Lab.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;

public class LabStorageView {
    private final BorderPane root;

    protected Button btnLoad, btnSave, btnRefresh;
    protected Button btnAddContainer, btnDeleteContainer;
    protected Button btnAddBox, btnDeleteBox, btnToggleOccupy;

    protected TableView<Container> containerTable;
    protected TableView<Box> boxTable;
    protected Label detailTitleLabel;
    protected Label containerInfoLabel;
    protected HBox detailActions;

    public LabStorageView() {
        root = new BorderPane();
        initVisualComponents();
    }

    private void initVisualComponents() {
        HBox topMenu = new HBox(10);
        topMenu.setPadding(new Insets(10));
        topMenu.setStyle("-fx-background-color: #e0e0e0;");

        btnLoad = new Button("Открыть файл");
        btnSave = new Button("Сохранить в файл");
        btnRefresh = new Button("Обновить (Refresh)");
        btnRefresh.setStyle("-fx-font-weight: bold;");

        topMenu.getChildren().addAll(btnLoad, btnSave, btnRefresh);
        root.setTop(topMenu);

        VBox masterLayout = new VBox(10);
        masterLayout.setPadding(new Insets(10));
        masterLayout.setPrefWidth(450);

        Label masterTitle = new Label("Лабораторные контейнеры");
        masterTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        containerTable = new TableView<>();
        setupContainerTable();

        HBox masterActions = new HBox(10);
        btnAddContainer = new Button("Добавить контейнер");
        btnDeleteContainer = new Button("Удалить контейнер");
        masterActions.getChildren().addAll(btnAddContainer, btnDeleteContainer);

        masterLayout.getChildren().addAll(masterTitle, containerTable, masterActions);
        VBox.setVgrow(containerTable, Priority.ALWAYS);

        VBox detailLayout = new VBox(10);
        detailLayout.setPadding(new Insets(10));
        detailLayout.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0 0 0 1;");

        detailTitleLabel = new Label("Детализация не выбрана");
        detailTitleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        containerInfoLabel = new Label("Выберите контейнер слева для просмотра содержимого.");
        containerInfoLabel.setWrapText(true);

        boxTable = new TableView<>();
        setupBoxTable();

        detailActions = new HBox(10);
        btnAddBox = new Button("📦 Создать бокс");
        btnDeleteBox = new Button("🗑️ Удалить бокс");
        btnToggleOccupy = new Button("🔒 Занять/Освободить");
        detailActions.getChildren().addAll(btnAddBox, btnDeleteBox, btnToggleOccupy);
        detailActions.setVisible(false);

        detailLayout.getChildren().addAll(detailTitleLabel, containerInfoLabel, boxTable, detailActions);
        VBox.setVgrow(boxTable, Priority.ALWAYS);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(masterLayout, detailLayout);
        splitPane.setDividerPositions(0.45);
        root.setCenter(splitPane);
    }

    private void setupContainerTable() {
        TableColumn<Container, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Container, String> nameCol = new TableColumn<>("Название");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Container, String> typeCol = new TableColumn<>("Тип");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Container, Integer> slotsCol = new TableColumn<>("Занято слотов");
        slotsCol.setCellValueFactory(new PropertyValueFactory<>("occupiedSlots"));

        containerTable.getColumns().addAll(idCol, nameCol, typeCol, slotsCol);
        containerTable.setPlaceholder(new Label("Нет доступных контейнеров."));
    }

    private void setupBoxTable() {
        TableColumn<Box, Long> idCol = new TableColumn<>("Box ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(70);

        TableColumn<Box, String> nameCol = new TableColumn<>("Название бокса");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Box, Integer> slotCol = new TableColumn<>("Слот №");
        slotCol.setCellValueFactory(new PropertyValueFactory<>("slotNumber"));

        TableColumn<Box, Boolean> occupiedCol = new TableColumn<>("Занят");
        occupiedCol.setCellValueFactory(new PropertyValueFactory<>("isOccupied"));

        boxTable.getColumns().addAll(idCol, nameCol, slotCol, occupiedCol);
        boxTable.setPlaceholder(new Label("В этом контейнере пока нет боксов."));
    }

    public BorderPane getRoot() {
        return root;
    }
}