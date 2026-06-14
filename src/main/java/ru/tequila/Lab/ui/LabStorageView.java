package ru.tequila.Lab.ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.domain.Box;

public class LabStorageView {
    public BorderPane root;

    public TableView<Container> containerTable;
    public Button btnAddContainer;
    public Button btnDeleteContainer;

    public TableView<Box> boxTable;
    public Button btnAddBox;
    public Button btnDeleteBox;

    public Button btnSave;
    public Button btnLoad;
    public Button btnRefresh;
    public Button btnShowHistory; // НОВАЯ КНОПКА

    public VBox detailActions;
    public Label detailTitleLabel;
    public Label containerInfoLabel;
    public Button btnToggleOccupy;

    public TextArea historyTextArea; // НОВОЕ ПОЛЕ ДЛЯ ВЫВОДА ЛОГОВ ИСТОРИИ

    public LabStorageView() {
        initVisualComponents();
    }

    @SuppressWarnings("unchecked")
    private void initVisualComponents() {
        root = new BorderPane();
        root.setPadding(new Insets(15));

        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(0, 0, 15, 0));
        topPanel.setAlignment(Pos.CENTER_LEFT);

        btnSave = new Button("Сохранить в файл");
        btnLoad = new Button("Загрузить из файла");
        btnRefresh = new Button("Обновить");
        btnShowHistory = new Button("История команд"); // Инициализация новой кнопки

        topPanel.getChildren().addAll(btnSave, btnLoad, btnRefresh, btnShowHistory);
        root.setTop(topPanel);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(45);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(55);
        grid.getColumnConstraints().addAll(col1, col2);

        VBox containerBox = new VBox(10);
        Label lblContainers = new Label("Список контейнеров:");
        lblContainers.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        containerTable = new TableView<>();

        TableColumn<Container, Long> cIdCol = new TableColumn<>("ID");
        cIdCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().id));
        cIdCol.setPrefWidth(50);

        TableColumn<Container, String> cNameCol = new TableColumn<>("Название");
        cNameCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().name));
        cNameCol.setPrefWidth(140);

        TableColumn<Container, Integer> cCapCol = new TableColumn<>("Вместимость");
        cCapCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().capacity));
        cCapCol.setPrefWidth(100);

        containerTable.getColumns().addAll(cIdCol, cNameCol, cCapCol);

        HBox containerButtons = new HBox(10);
        btnAddContainer = new Button("Добавить");
        btnDeleteContainer = new Button("Удалить");
        btnDeleteContainer.getStyleClass().add("button-delete");

        containerButtons.getChildren().addAll(btnAddContainer, btnDeleteContainer);
        containerBox.getChildren().addAll(lblContainers, containerTable, containerButtons);
        grid.add(containerBox, 0, 0);

        VBox rightLayout = new VBox(10);
        Label lblBoxes = new Label("Управление элементами:");
        lblBoxes.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        HBox splitBoxPanel = new HBox(15);

        boxTableSubLayout = new VBox(10);
        boxTable = new TableView<>();

        TableColumn<Box, Long> bIdCol = new TableColumn<>("ID");
        bIdCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().id));
        bIdCol.setPrefWidth(50);

        TableColumn<Box, String> bNumCol = new TableColumn<>("Номер/Имя");
        bNumCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().name));
        bNumCol.setPrefWidth(90);

        TableColumn<Box, Boolean> bOccCol = new TableColumn<>("Занят");
        bOccCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().isOccupied));
        bOccCol.setPrefWidth(70);

        boxTable.getColumns().addAll(bIdCol, bNumCol, bOccCol);

        HBox boxButtons = new HBox(10);
        btnAddBox = new Button("Бокс");
        btnDeleteBox = new Button("Удалить");
        btnDeleteBox.getStyleClass().add("button-delete");
        boxButtons.getChildren().addAll(btnAddBox, btnDeleteBox);
        VBox boxTableLayout = new VBox(10);
        boxTableLayout.getChildren().addAll(boxTable, boxButtons);

        detailActions = new VBox(12);
        detailActions.setPadding(new Insets(10));
        detailActions.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e2ede7; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        detailActions.setPrefWidth(220);

        detailTitleLabel = new Label("Детальная инфо:");
        detailTitleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        containerInfoLabel = new Label("Выберите контейнер...");
        containerInfoLabel.setWrapText(true);

        btnToggleOccupy = new Button("Изменить статус");

        detailActions.getChildren().addAll(detailTitleLabel, containerInfoLabel, btnToggleOccupy);

        splitBoxPanel.getChildren().addAll(boxTableLayout, detailActions);
        rightLayout.getChildren().addAll(lblBoxes, splitBoxPanel);

        grid.add(rightLayout, 1, 0);
        root.setCenter(grid);

        VBox bottomPanel = new VBox(5);
        bottomPanel.setPadding(new Insets(10, 0, 0, 0));
        Label lblHistory = new Label("Лог последних действий:");
        lblHistory.setStyle("-fx-font-weight: bold; -fx-text-fill: #7f8c8d;");
        historyTextArea = new TextArea();
        historyTextArea.setPrefHeight(100);
        historyTextArea.setEditable(false); // Запрещаем пользователю редактировать лог вручную
        bottomPanel.getChildren().addAll(lblHistory, historyTextArea);

        root.setBottom(bottomPanel);
    }

    private VBox boxTableSubLayout;

    public BorderPane getRoot() {
        return root;
    }
}