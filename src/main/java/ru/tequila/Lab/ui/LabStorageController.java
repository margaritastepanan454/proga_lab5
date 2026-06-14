package ru.tequila.Lab.ui;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import ru.tequila.Lab.domain.Box;
import ru.tequila.Lab.domain.Container;
import ru.tequila.Lab.repository.ContainerRepository;
import ru.tequila.Lab.service.ContainerService;
import ru.tequila.Lab.service.HistoryService;
import java.util.List;

public class LabStorageController {
    private final LabStorageView view;
    private final ContainerRepository repository;
    private final ContainerService containerService;
    private final HistoryService historyService;
    private Container selectedContainer;

    public LabStorageController(LabStorageView view, ContainerRepository repository,
                                ContainerService containerService, HistoryService historyService) {
        this.view = view;
        this.repository = repository;
        this.containerService = containerService;
        this.historyService = historyService;

        initEventHandlers();
        refreshUIData();
    }

    private void initEventHandlers() {
        view.containerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedContainer = newSel;
            if (newSel != null) {
                updateDetailPanel(newSel);
                view.detailActions.setVisible(true);
            } else {
                clearDetailPanel();
                view.detailActions.setVisible(false);
            }
        });

        view.btnRefresh.setOnAction(e -> refreshUIData());
        view.btnAddContainer.setOnAction(e -> handleAddContainer());
        view.btnDeleteContainer.setOnAction(e -> handleDeleteContainer());
        view.btnAddBox.setOnAction(e -> handleAddBox());
        view.btnDeleteBox.setOnAction(e -> handleDeleteBox());
        view.btnToggleOccupy.setOnAction(e -> handleToggleOccupy());
        view.btnShowHistory.setOnAction(e -> handleShowHistory());

        // Отключаем кнопки работы с файлами
        view.btnSave.setDisable(true);
        view.btnLoad.setDisable(true);
    }

    private void refreshUIData() {
        view.containerTable.setItems(FXCollections.observableArrayList(repository.findAllContainers()));
        if (selectedContainer != null) {
            Container updated = repository.findContainerById(selectedContainer.id);
            if (updated != null) updateDetailPanel(updated);
            else clearDetailPanel();
        }
    }

    private void updateDetailPanel(Container container) {
        view.detailTitleLabel.setText("Контейнер: " + container.name);
        view.containerInfoLabel.setText(String.format("Тип: %s | Локация: %s | Статус: %s | Слоты: %d/%d",
                container.type, container.location, container.status, container.occupiedSlots, container.capacity));
        view.boxTable.setItems(FXCollections.observableArrayList(repository.findBoxesByContainer(container.id)));
    }

    private void clearDetailPanel() {
        selectedContainer = null;
        view.detailTitleLabel.setText("Детализация не выбрана");
        view.boxTable.getItems().clear();
    }

    private void handleAddContainer() {
        ContainerDialog dialog = new ContainerDialog(0);
        dialog.showAndWait().ifPresent(container -> {
            try {
                if (container.name == null || container.name.trim().isEmpty()) {
                    throw new IllegalArgumentException("Имя контейнера не может быть пустым!");
                }
                repository.saveContainer(container);

                if (historyService != null) {
                    historyService.addCommand("Добавлен новый контейнер в БД: " + container.name);
                }
                refreshUIData();
            } catch (Exception ex) {
                showError("Ошибка валидации", ex.getMessage());
            }
        });
    }

    private void handleAddBox() {
        if (selectedContainer == null) return;
        BoxDialog dialog = new BoxDialog(0, selectedContainer.id);
        dialog.showAndWait().ifPresent(box -> {
            try {
                if (box.name == null || box.name.trim().isEmpty()) {
                    throw new IllegalArgumentException("Название бокса не может быть пустым!");
                }
                repository.saveBox(box);

                if (historyService != null) {
                    historyService.addCommand(String.format("В контейнер '%s' добавлен бокс: %s", selectedContainer.name, box.name));
                }
                refreshUIData();
            } catch (Exception ex) {
                showError("Ошибка создания бокса", ex.getMessage());
            }
        });
    }

    private void handleDeleteContainer() {
        if (selectedContainer == null) return;
        String name = selectedContainer.name;
        repository.deleteContainerById(selectedContainer.id);

        if (historyService != null) {
            historyService.addCommand("Удален контейнер из БД: " + name);
        }
        refreshUIData();
    }

    private void handleDeleteBox() {
        Box box = view.boxTable.getSelectionModel().getSelectedItem();
        if (box == null) return;
        String boxName = box.name;
        repository.deleteBoxById(box.id);

        if (historyService != null) {
            historyService.addCommand("Удален бокс из БД: " + boxName);
        }
        refreshUIData();
    }

    private void handleToggleOccupy() {
        Box box = view.boxTable.getSelectionModel().getSelectedItem();
        if (box == null) return;

        box.isOccupied = !box.isOccupied;
        repository.saveBox(box);

        if (historyService != null) {
            historyService.addCommand((box.isOccupied ? "Занят" : "Освобожден") + " бокс: " + box.name);
        }
        refreshUIData();
    }

    public void handleShowHistory() {
        if (historyService == null) return;
        List<String> history = historyService.getHistory();
        StringBuilder sb = new StringBuilder("=== ИСТОРИЯ ПОСЛЕДНИХ КОМАНД ===\n");
        if (history.isEmpty()) {
            sb.append("История команд пуста.");
        } else {
            for (int i = 0; i < history.size(); i++) {
                sb.append((i + 1)).append(". ").append(history.get(i)).append("\n");
            }
        }
        if (view.historyTextArea != null) view.historyTextArea.setText(sb.toString());
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}